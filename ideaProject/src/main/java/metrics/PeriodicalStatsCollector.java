package metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.BenchConsts;
import util.Formatter;
import java.util.List;

public class PeriodicalStatsCollector implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(PeriodicalStatsCollector.class);

    private final List<ProducerStats> statsList;
    private final int benchDuration;
    private final int warmupDuration;

    public PeriodicalStatsCollector(List<ProducerStats> producers, int benchDuration, int warmupDuration) {
        this.statsList = producers;
        this.benchDuration = benchDuration;
        this.warmupDuration = warmupDuration;
    }

    @Override
    public void run() {
        try {
            BenchConsts.BENCH_DURATION_UNIT.sleep(warmupDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long startTime = System.nanoTime();
        long endTime = startTime + BenchConsts.BENCH_DURATION_UNIT.toNanos(benchDuration);
        LOGGER.info("Bench start time: {}. Bench end time: {}", startTime, endTime);
        long startPeriod = System.nanoTime();
        while (true) {

            LOGGER.info("Current start period: {}", startPeriod);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                break;
            }
            long stopPeriod = System.nanoTime();
            double elapsed = (stopPeriod - startPeriod) / 1e9;
            for (ProducerStats stats : statsList) {
                stats.producerRates.add(Formatter.round(stats.periodicalMessageCount / elapsed));
                stats.producerThroughputs.add(Formatter.round(stats.periodicalByteCount / elapsed / (1024 * 1024)));
                stats.resetPeriodicalStats();
            }
            if (stopPeriod >= endTime) {
                break;
            }
            startPeriod = stopPeriod;
        }
    }
}
