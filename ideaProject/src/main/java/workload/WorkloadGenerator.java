package workload;

import kafka.KafkaService;
import metrics.PeriodicalStatsCollector;
import metrics.ProducerStats;
import metrics.MetricsManager;
import metrics.ProducerMetrics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.StringGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static util.BenchConsts.*;

public class WorkloadGenerator {

    private static final Logger LOGGER = LogManager.getLogger(WorkloadGenerator.class);

    private final WorkloadConfig workloadConfig;
    private final KafkaService kafkaService;
    private final ScheduledThreadPoolExecutor executorService;
    private final int benchmarkDuration;
    private final int warmupDuration;

    private List<ScheduledFuture<?>> futures = new ArrayList<>();
    private final List<ProducerMetrics> producersMetrics = new ArrayList<>();
    private final List<ProducerStats> producersStats = new ArrayList<>();
    private final int producerRate;

    public WorkloadGenerator(WorkloadConfig workloadConfig, KafkaService kafkaService) {
        this.workloadConfig = workloadConfig;
        this.kafkaService = kafkaService;
        this.executorService = new ScheduledThreadPoolExecutor(workloadConfig.getNumberOfTopics());
        this.producerRate = workloadConfig.getProducerRatePerSec();
        this.benchmarkDuration = workloadConfig.getBenchmarkDurationMinutes();
        this.warmupDuration = workloadConfig.getWarmupDurationMinutes();
    }

    public void generateLoad() {
        initializeKafka();
        startPeriodicalCollector();
        generateWorkload();
        enterWarmupPhase();
        enterBenchPhase();
        stopLoad();
        shutdown();
        prepareResults();
    }

    private void startPeriodicalCollector() {
        kafkaService.getProducers().forEach(
                producer -> producersStats.add(producer.getLocalStats()));
        PeriodicalStatsCollector statsCollector = new PeriodicalStatsCollector(producersStats, benchmarkDuration, warmupDuration);
        executorService.submit(statsCollector);
    }

    private void prepareResults() {
        // Find a way to make a better separation of concerns here...
        kafkaService.getProducers().forEach(
                producer -> producersMetrics.add(new ProducerMetrics(producer)));
        Map<String, String> producerPros = kafkaService.getProducerProperties();
        String benchmarkName = kafkaService.getKafkaConfigName() + "," +
                workloadConfig.getName();
        MetricsManager metricsManager =
                new MetricsManager(producersMetrics, producersStats);
        metricsManager.writeMetrics(false, workloadConfig, producerPros, benchmarkName);
    }

    private void initializeKafka() {
        Set<String> topics = kafkaService.createTopics(workloadConfig.getNumberOfTopics(),
                workloadConfig.getPartitionsPerTopic());
        LOGGER.info("Created {} topics", topics.size());
        kafkaService.createProducers(topics);
        LOGGER.info("Created {} producers", kafkaService.getProducers().size());
    }

    private void enterBenchPhase() {
        LOGGER.info("Starting the benchmark. It'll run for {} minutes", benchmarkDuration);
        try {
            BENCH_DURATION_UNIT.sleep(benchmarkDuration);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }

    private void enterWarmupPhase() {
        LOGGER.info("Getting warm-up for {} minutes", warmupDuration);
        try {
            BENCH_DURATION_UNIT.sleep(warmupDuration);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
        kafkaService.getProducers().forEach(p -> p.setWarmup(false));
    }

    private void stopLoad() {
        LOGGER.info("Finished producing workload. Shutting down...");
        executorService.submit(() -> futures.forEach(f -> f.cancel(true)));

    }

    /**
     * Each run() method of the corresponding thread (KafkaBenchProducer)
     * produces (sends) a single message to the kafka broker.
     * This function provides a possibility to control the producer rate.
     * For example, scheduling the run() method with 1ms period means that
     * a producer will send approximately 1000 messages in a second.
     */
    private void generateWorkload() {
        long period = calculatePeriodForGivenProducerRate(producerRate);
        futures = kafkaService.getProducers()
                .stream()
                .map(producer -> executorService.scheduleAtFixedRate(
                        producer,
                        DEFAULT_INITIAL_DELAY_OF_PRODUCERS,
                        period,
                        TimeUnit.NANOSECONDS
                )).collect(Collectors.toList());
    }

    private long calculatePeriodForGivenProducerRate(int producerRate) {
        return (NANOS_IN_SECOND / producerRate);
    }

    private void shutdown() {
        kafkaService.shutdown();
        executorService.shutdown();
    }
}
