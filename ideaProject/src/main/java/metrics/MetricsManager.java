package metrics;

import bench.BenchmarkResult;
import bench.PeriodicResult;
import bench.ResBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.CsvWriter;
import util.MissingConfigInfoException;
import util.PeriodicalRateCsvWriter;
import workload.WorkloadConfig;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class MetricsManager {

    private static final Logger LOGGER = LogManager.getLogger(MetricsManager.class);

    private List<ProducerMetrics> producerMetricsList;
    private List<ProducerStats> producerStatsList;

    public MetricsManager(List<ProducerMetrics> producerMetricsList,
                          List<ProducerStats> producerStatsList) {
        this.producerMetricsList = producerMetricsList;
        this.producerStatsList = producerStatsList;
    }


    public void writeMetrics(boolean defaultSetting, WorkloadConfig workloadConfig,
                             Map<String, String> producerProps, String benchName) {
        writeAveMetrics(defaultSetting, workloadConfig, producerProps, benchName);
        writePeriodicMetrics(benchName, producerStatsList);
    }

    private void writeAveMetrics(boolean defaultSetting, WorkloadConfig workloadConfig, Map<String, String> producerProps, String benchName) {
        ResBuilder resBuilder = new ResBuilder();
        setBenchmarkSettings(defaultSetting, workloadConfig, producerProps, benchName, resBuilder);
        setManualResults(workloadConfig, resBuilder);
        setResultsFromProducerMetrics(resBuilder);
        BenchmarkResult benchmarkResult = new BenchmarkResult(resBuilder);
        CsvWriter.toCsv(benchmarkResult);
    }

    private void setResultsFromProducerMetrics(ResBuilder resBuilder) {
        // ave from producer metrics
        Map<String, Double> summed = producerMetricsList
                .stream()
                .map(ProducerMetrics::getRelevantMetrics)
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(
                        Map.Entry::getValue)));
        summed = summed.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> Math.round(e.getValue()*100.0)/100.0
        ));
        resBuilder.setResultsFromProducerMetricsMethod(summed);
    }

    private void setManualResults(WorkloadConfig workloadConfig, ResBuilder resBuilder) {
        // Estimated throughput
        double sumBytesSent =
                producerStatsList.stream().map(stats -> stats.periodicalByteCount).mapToDouble(d->d).sum();
        double aveLatency = producerStatsList.stream()
                .map(stats -> stats.latencies)
                .map(list -> list.stream().mapToLong(l->l).average().orElse(0))
                .mapToDouble(l -> l)
                .average()
                .orElse(0d);
        double maxLatency = producerStatsList.stream()
                .map(stats -> stats.latencies)
                .map(list -> list.stream().mapToLong(l->l).max().orElse(0))
                .mapToDouble(l -> l)
                .average()
                .orElse(0d);

        double estimatedThroughput = sumBytesSent / (workloadConfig.getBenchmarkDurationMinutes()*60) / (1024*1024);
        resBuilder.setResults(estimatedThroughput, aveLatency, maxLatency);
    }

    private void setBenchmarkSettings(boolean defaultSetting, WorkloadConfig workloadConfig,
                                      Map<String, String> producerProps, String benchName, ResBuilder resBuilder) {
        resBuilder.withName(benchName);
        if (defaultSetting) {
            resBuilder.withDefaultSetting();
        } else {
            if (workloadConfig == null || producerProps.isEmpty()) {
                throw new MissingConfigInfoException("None of config info can be null or empty" +
                        " if defaultSetting flag is set to false.");
            } else {
                resBuilder.withCustomSetting(
                        workloadConfig.getPartitionsPerTopic(),
                        producerProps.get("acks"),
                        Integer.parseInt(producerProps.get("batch.size")),
                        Integer.parseInt(producerProps.get("linger.ms")));
            }
        }
    }

    private void writePeriodicMetrics(String benchName, List<ProducerStats> statsList) {

        List<Double> summedRates = IntStream.range(0, statsList.get(0).producerRates.size())
                .mapToObj(i -> statsList.stream()
                        .mapToDouble(l -> l.producerRates.get(i))
                        .sum())
                .collect(Collectors.toList());
        List<Double> summedThroughputs = IntStream.range(0, statsList.get(0).producerThroughputs.size())
                .mapToObj(i -> statsList.stream()
                        .mapToDouble(l -> l.producerThroughputs.get(i))
                        .sum())
                .collect(Collectors.toList());
        PeriodicResult periodicResult = new PeriodicResult(benchName, summedRates, summedThroughputs);
        PeriodicalRateCsvWriter.toCsv(periodicResult);
    }

}
