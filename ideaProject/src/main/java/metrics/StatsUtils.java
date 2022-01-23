package metrics;

import java.util.OptionalDouble;

public class StatsUtils {

    private StatsUtils() {}

    public static double getAveLatency(ProducerStats producerStats) {
        OptionalDouble optionalAve = producerStats.latencies.stream().mapToDouble(l -> l).average();
        if (optionalAve.isPresent()) {
            return optionalAve.getAsDouble();
        } else {
            return 0d;
        }
    }
}
