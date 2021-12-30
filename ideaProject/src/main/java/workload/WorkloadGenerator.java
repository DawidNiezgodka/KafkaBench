package workload;

import kafka.KafkaBenchProducer;
import kafka.KafkaService;
import metrics.MetricsHandler;
import metrics.ProducerMetrics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.StringGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static util.BenchConsts.*;

public class WorkloadGenerator {

    private static final Logger LOGGER = LogManager.getLogger(WorkloadGenerator.class);

    private final WorkloadConfig workloadConfig;
    private final KafkaService kafkaService;
    private final ScheduledThreadPoolExecutor executorService;
    private final StringGenerator stringGenerator;
    private final int benchmarkDuration;
    private final int warmupDuration;

    private List<ScheduledFuture<?>> futures = new ArrayList<>();
    private List<ProducerMetrics> producersMetrics = new ArrayList<>();
    private int producerRate;
    private int totalExpectedRecordsNumber = 0;

    public WorkloadGenerator(WorkloadConfig workloadConfig, KafkaService kafkaService) {
        this.workloadConfig = workloadConfig;
        this.kafkaService = kafkaService;
        this.executorService = new ScheduledThreadPoolExecutor(workloadConfig.getNumberOfTopics());
        this.stringGenerator = new StringGenerator();
        this.producerRate = workloadConfig.getProducerRatePerSec();
        this.benchmarkDuration = workloadConfig.getBenchmarkDurationMinutes();
        this.warmupDuration = workloadConfig.getWarmupDuration();
    }

    public void runBenchmark() {

        Set<String> topics = kafkaService.createTopics(workloadConfig.getNumberOfTopics(),
                workloadConfig.getPartitionsPerTopic());
        LOGGER.info("Created {} topics", topics.size());
        String payload = stringGenerator.createPayloadRandomly(workloadConfig.getMessageSize());
        LOGGER.info("Created payload of the size {}", payload.length());
        kafkaService.createProducers(topics);
        LOGGER.info("Created {} producers", kafkaService.getProducers().size());
        // TODO: create consumers and check their readiness
        //consumerManager.createConsumers(topics);
        //consumerManager.ensureConsumersReadiness();
        if (producerRate == 0) {
            // TODO: find max. producer rate
        }

        LOGGER.info("Getting warm-up for {} minutes", warmupDuration);
        generateWorkload();
        // TODO: Reset metrics?
        LOGGER.info("Starting the benchmark. It'll run for {} minutes", benchmarkDuration);
        // TODO: how to stop it after the given time?
        // collectStatsFor15m() -> after a specific amount of time elapses this will collect metrics, return and shutdown
        stopLoad();

        try {
            //BENCH_DURATION_UNIT.sleep(
             //       benchmarkDuration + EXTRA_SHUTDOWN_TIME);
            TimeUnit.SECONDS.sleep(
                    15 + 5);

        } catch (InterruptedException e) {
            LOGGER.info("Something went wrong while waiting for threads to shutdown...");
        }

        LOGGER.info("Finished producing workload. Shutting down...");
        kafkaService.getProducers().forEach(
                producer -> producersMetrics.add(
                        new ProducerMetrics(producer)
                )
        );
        totalExpectedRecordsNumber = kafkaService.getProducers().stream()
                        .map(KafkaBenchProducer::getProducedMessages).mapToInt(i->i).sum();

        MetricsHandler metricsHandler = new MetricsHandler(producersMetrics, totalExpectedRecordsNumber);
        metricsHandler.writeMetrics();
        shutdown();
    }

    private void stopLoad() {
        executorService.schedule(
                () -> futures.forEach(f -> f.cancel(true)),
                15, TimeUnit.SECONDS);
    }
    //benchmarkDuration, BENCH_DURATION_UNIT

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

    /*
    Starting level: 1 message / s
     */
    private long calculatePeriodForGivenProducerRate(int producerRate) {
        return (NANOS_IN_SECOND / producerRate);
    }

    private void shutdown() {
        kafkaService.shutdown();
        executorService.shutdown();
        // output results
    }
}
