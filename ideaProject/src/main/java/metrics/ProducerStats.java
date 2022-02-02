package metrics;

import java.util.ArrayList;
import java.util.List;

public class ProducerStats {

    public long periodicalFailedCount = 0;
    public long periodicalMessageCount = 0;
    public long periodicalByteCount = 0;

    public long totalFailedCount = 0;
    public long totalMessageCount = 0;
    public long totalByteCount = 0;

    public List<Long> latencies = new ArrayList<>();
    public List<Double> producerRates = new ArrayList<>();
    public List<Double> producerThroughputs = new ArrayList<>();


    public void resetPeriodicalStats() {
        periodicalFailedCount = 0;
        periodicalByteCount = 0;
        periodicalMessageCount = 0;
    }
}
