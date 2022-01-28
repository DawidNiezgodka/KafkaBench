package bench;

public class BenchmarkResult {

    private final String name;
    /**
     * Producer and topic config
     */
    private final int partitions;
    private final String acks;
    private final int batchSize;
    private final int lingerMs;

    /**
     * Manually computed metrics
     */
    private final double publishRate;

    private final int consumeRate;
    private final int backlog;

    private final double aveLatency;
    private final double maxLatency;
    private final double aveEndToEndLatency;
    private final double maxEndToEndLatency;

    /**
     * Metrics from the KafkaProducer's metrics() method
     */
    private final double reqLatAvg;
    private final double reqLatMax;
    private final double batchSizeAvg;
    private final double recSendTotal;
    private final double recSendRate;

    public BenchmarkResult(ResBuilder resBuilder) {
        this.name = resBuilder.getName();
        this.publishRate = resBuilder.getPublishRate();
        this.consumeRate = resBuilder.getConsumeRate();
        this.backlog = resBuilder.getBacklog();
        this.partitions = resBuilder.getPartitions();
        this.acks = resBuilder.getAcks();
        this.batchSize = resBuilder.getBatchSize();
        this.lingerMs = resBuilder.getLingerMs();
        this.aveLatency = resBuilder.getAveLatency();
        this.maxLatency = resBuilder.getMaxLatency();
        this.aveEndToEndLatency = resBuilder.getAveEndToEndLatency();
        this.maxEndToEndLatency = resBuilder.getMaxEndToEndLatency();
        this.reqLatAvg = resBuilder.getReqLatAvg();
        this.reqLatMax = resBuilder.getReqLatMax();
        this.batchSizeAvg = resBuilder.getBatchSizeAvg();
        this.recSendTotal = resBuilder.getRecSendTotal();
        this.recSendRate = resBuilder.getRecSendRate();
    }

    public int getPartitions() {
        return partitions;
    }

    public String getAcks() {
        return acks;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public int getLingerMs() {
        return lingerMs;
    }

    public double getPublishRate() {
        return publishRate;
    }

    public int getConsumeRate() {
        return consumeRate;
    }

    public int getBacklog() {
        return backlog;
    }

    public double getAveLatency() {
        return aveLatency;
    }

    public double getMaxLatency() {
        return maxLatency;
    }

    public double getAveEndToEndLatency() {
        return aveEndToEndLatency;
    }

    public double getMaxEndToEndLatency() {
        return maxEndToEndLatency;
    }

    public double getReqLatAvg() {
        return reqLatAvg;
    }

    public double getReqLatMax() {
        return reqLatMax;
    }

    public double getBatchSizeAvg() {
        return batchSizeAvg;
    }

    public double getRecSendTotal() {
        return recSendTotal;
    }

    public double getRecSendRate() {
        return recSendRate;
    }

    public String getName() {
        return name;
    }
}
