package metrics;

import bench.BenchmarkResult;
import bench.ResBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.CsvWriter;
import util.MissingConfigInfoException;
import workload.WorkloadConfig;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetricsManager {

    private static final Logger LOGGER = LogManager.getLogger(MetricsManager.class);

    private List<ProducerMetrics> producerMetricsList;
    private List<ProducerStats> producerStatsList;

    public MetricsManager(List<ProducerMetrics> producerMetricsList,
                          List<ProducerStats> producerStatsList) {
        this.producerMetricsList = producerMetricsList;
        this.producerStatsList = producerStatsList;
        // todo remove this
        for (ProducerStats s : producerStatsList) {
            System.out.println("Message count: " + s.messageCounter);
        }
    }


    public void writeMetrics(boolean defaultSetting, WorkloadConfig workloadConfig,
                             Map<String, String> producerProps, String benchName) {
        ResBuilder resBuilder = new ResBuilder();
        setBenchmarkSettings(defaultSetting, workloadConfig, producerProps, benchName, resBuilder);
        setManualResults(workloadConfig, resBuilder);
        setResultsFromProducerMetrics(resBuilder);
        BenchmarkResult benchmarkResult = new BenchmarkResult(resBuilder);
        CsvWriter.toCsv(benchmarkResult);
    }

    private void setResultsFromProducerMetrics(ResBuilder resBuilder) {
        // ave from producer metrics
        Map<String, Double> averagedMetrics = producerMetricsList
                .stream()
                .map(ProducerMetrics::getRelevantMetrics)
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.averagingDouble(Map.Entry::getValue)));
        resBuilder.setResultsFromProducerMetricsMethod(averagedMetrics);
    }

    private void setManualResults(WorkloadConfig workloadConfig, ResBuilder resBuilder) {
        // Estimated throughput
        double aveBytesSent =
                producerStatsList.stream().map(stats -> stats.byteCounter).mapToDouble(d->d).average().orElse(0d);
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

        double estimatedThroughput = aveBytesSent / (workloadConfig.getBenchmarkDurationMinutes()*60) / (1024*1024);
        resBuilder.setResults(estimatedThroughput, aveLatency, maxLatency);
    }

    private void setBenchmarkSettings(boolean defaultSetting, WorkloadConfig workloadConfig,
                                      Map<String, String> producerProps, String benchName, ResBuilder resBuilder) {
        resBuilder.withName(benchName);
        if (defaultSetting) {
            resBuilder.withDefaultSetting();
        } else {
            if (workloadConfig == null || producerProps.isEmpty()) {
                throw new MissingConfigInfoException("None of config info can be null or empty if defaultSetting flag is set to false.");
            } else {
                resBuilder.withCustomSetting(
                        workloadConfig.getPartitionsPerTopic(),
                        producerProps.get("acks"),
                        Integer.parseInt(producerProps.get("batch.size")),
                        Integer.parseInt(producerProps.get("linger.ms")));
            }
        }
    }

}
