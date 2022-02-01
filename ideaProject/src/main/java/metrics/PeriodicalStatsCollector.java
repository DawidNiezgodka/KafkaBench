package metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.BenchConsts;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PeriodicalStatsCollector implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(PeriodicalStatsCollector.class);

    private final List<ProducerStats> statsList;
    private final long benchDuration;

    public PeriodicalStatsCollector(List<ProducerStats> producers, long benchDuration) {
        this.statsList = producers;
        this.benchDuration = benchDuration;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        long endTime = startTime + TimeUnit.SECONDS.toNanos(15);
        //long endTime = startTime + BenchConsts.BENCH_DURATION_UNIT.toNanos(benchDuration);
        LOGGER.info("Bench start time: {}. Bench end time: {}", startTime, endTime);
        long startPeriod = System.nanoTime();
        while (true) {

            LOGGER.info("Current start period: {}", startPeriod);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                break;
            }
            long stopPeriod = System.nanoTime();
            double elapsed = (stopPeriod - startPeriod) / 1e9;
            for (ProducerStats stats : statsList) {
                stats.producerRates.add(stats.periodicalMessageCount / elapsed);
                stats.producerThroughputs.add(stats.periodicalByteCount / elapsed / (1024 * 1024));
                stats.resetPeriodicalStats();
            }
            if (stopPeriod >= endTime) {
                break;
            }
            startPeriod = stopPeriod;
        }
    }
}
