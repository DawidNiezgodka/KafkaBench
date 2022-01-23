package metrics;

import java.util.ArrayList;
import java.util.List;

public class ProducerStats {

    // Stats for actual bench
    public long failedCount = 0;
    public long messageCounter = 0;
    public long byteCounter = 0;

    // Stats for actual bench + warm-up
    public long totalFailedCount = 0;
    public long totalMessageCounter = 0;
    public long totalByteCounter = 0;

    public List<Long> latencies = new ArrayList<>();

}
