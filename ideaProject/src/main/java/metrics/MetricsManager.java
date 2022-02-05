package metrics;

import bench.BenchmarkResult;
import bench.ResBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.BenchConsts;
import util.CsvWriter;
import util.Formatter;
import util.MissingConfigInfoException;
import workload.WorkloadConfig;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MetricsManager {

    private static final Logger LOGGER = LogManager.getLogger(MetricsManager.class);

    private final List<ProducerMetrics> producerMetricsList;
    private final List<ProducerStats> producerStatsList;

    public MetricsManager(List<ProducerMetrics> producerMetricsList,
                          List<ProducerStats> producerStatsList) {
        this.producerMetricsList = producerMetricsList;
        this.producerStatsList = producerStatsList;
    }


    public void writeMetrics(boolean defaultSetting, WorkloadConfig workloadConfig,
                             Map<String, String> producerProps, String benchName) {
        writeAllMetrics(defaultSetting, workloadConfig, producerProps, benchName);
    }

    private void writeAllMetrics(boolean defaultSetting,
                                 WorkloadConfig workloadConfig, Map<String, String> producerProps, String benchName) {
        ResBuilder resBuilder = new ResBuilder();
        setBenchmarkSettings(defaultSetting, workloadConfig, producerProps, benchName, resBuilder);
        setManualResults(workloadConfig, resBuilder);
        setResultsFromProducerMetrics(resBuilder);
        setPeriodicMetrics(resBuilder);
        BenchmarkResult benchmarkResult = new BenchmarkResult(resBuilder);
        CsvWriter.toCsv(benchmarkResult);
    }

    private void setResultsFromProducerMetrics(ResBuilder resBuilder) {
        // ave from producer metrics
        Map<String, Double> summed = producerMetricsList
                .stream()
                .map(ProducerMetrics::getRelevantMetrics)
                .flatMap(m -> m.entrySet().stream())
                .filter(metric -> BenchConsts.SUMMED_PRODUCER_METRICS.contains(metric.getKey()))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(
                        Map.Entry::getValue)));
        Map<String, Double> averaged = producerMetricsList
                .stream()
                .map(ProducerMetrics::getRelevantMetrics)
                .flatMap(m -> m.entrySet().stream())
                .filter(metric -> BenchConsts.AVERAGED_PRODUCER_METRICS.contains(metric.getKey()))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.averagingDouble(
                        Map.Entry::getValue)));
        Stream<Map.Entry<String, Double>> combined = Stream.concat(summed.entrySet().stream(), averaged.entrySet().stream());
        Map<String, Double> result = combined.collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        result = result.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> Formatter.round(e.getValue())
        ));
        resBuilder.setResultsFromProducerMetricsMethod(result);
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

        long sentMessages = producerStatsList.stream()
                .map(stats -> stats.totalMessageCount)
                .mapToLong(l -> l)
                .sum();

        long failedMessages = producerStatsList.stream()
                .map(stats -> stats.totalFailedCount)
                .mapToLong(l -> l)
                .sum();

        double estimatedThroughput = sumBytesSent / (workloadConfig.getBenchmarkDurationMinutes()*60) / (1024*1024);
        resBuilder.setResults(Formatter.round(estimatedThroughput),
                Formatter.round(aveLatency), Formatter.round(maxLatency), sentMessages, failedMessages);
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

    private void setPeriodicMetrics(ResBuilder resBuilder) {

        List<Double> summedRates = IntStream.range(0, producerStatsList.get(0).producerRates.size())
                .mapToObj(i -> producerStatsList.stream()
                        .mapToDouble(l -> l.producerRates.get(i))
                        .sum())
                .collect(Collectors.toList());
        List<Double> summedThroughputs = IntStream.range(0, producerStatsList.get(0).producerThroughputs.size())
                .mapToObj(i -> producerStatsList.stream()
                        .mapToDouble(l -> l.producerThroughputs.get(i))
                        .sum())
                .collect(Collectors.toList());

        String producerRatesRes = summedRates.stream().map(Object::toString).collect(Collectors.joining(","));
        String producerThroughputsRes = summedThroughputs.stream().map(Object::toString).collect(Collectors.joining(","));
        resBuilder.setPeriodicalResultStrings(producerRatesRes, producerThroughputsRes);
    }
}
