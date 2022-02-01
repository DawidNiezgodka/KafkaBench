package bench;

import java.util.Map;

public class ResBuilder {

    private String name;

    private double publishRate;

    private int partitions;
    private String acks;
    private int batchSize;
    private int lingerMs;

    private double aveLatency;
    private double maxLatency;

    private double reqLatAvg;
    private double reqLatMax;
    private double batchSizeAvg;
    private double recSendTotal;
    private double recSendRate;

    public ResBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ResBuilder withDefaultSetting() {
        this.partitions = 1;
        this.acks = "0";
        this.lingerMs = 0;
        this.batchSize = 16384;
        return this;
    }

    public ResBuilder withCustomSetting(int partitions, String acks, int batchSize, int lingerMs) {
        this.partitions = partitions;
        this.acks = acks;
        this.batchSize = batchSize;
        this.lingerMs = lingerMs;
        return this;
    }

    public ResBuilder setResults(double publishRate, double aveLatency, double maxLatency) {
        this.publishRate = publishRate;
        this.aveLatency = aveLatency;
        this.maxLatency = maxLatency;
        return this;
    }

    public ResBuilder setResultsFromProducerMetricsMethod(Map<String, Double> averagedMetrics) {
        this.reqLatAvg = averagedMetrics.get("request-latency-avg");
        this.reqLatMax = averagedMetrics.get("request-latency-max");
        this.batchSizeAvg = averagedMetrics.get("batch-size-avg");
        this.recSendTotal = averagedMetrics.get("record-send-total");
        this.recSendRate = averagedMetrics.get("record-send-rate");
        return this;
    }

    public ResBuilder withPublishRate(double publishRate) {
        this.publishRate = publishRate;
        return this;
    }

    public ResBuilder withAveLat(int aveLat) {
        this.aveLatency = aveLat;
        return this;
    }

    public ResBuilder withMaxLat(int maxLat) {
        this.maxLatency = maxLat;
        return this;
    }

    public BenchmarkResult create() {
        return new BenchmarkResult(this);
    }

    public double getPublishRate() {
        return publishRate;
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

    public double getAveLatency() {
        return aveLatency;
    }

    public double getMaxLatency() {
        return maxLatency;
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
