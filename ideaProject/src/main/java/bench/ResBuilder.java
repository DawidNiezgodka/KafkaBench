package bench;

import java.util.Map;

public class ResBuilder {

    private String name;

    private int partitions;
    private String acks;
    private int batchSize;
    private int lingerMs;

    private double aveLatency;
    private double maxLatency;
    private String producerRates;
    private String producerThroughputs;
    private double publishRate;

    private double reqLatAvg;
    private double reqLatMax;
    private double batchSizeAvg;
    private double recSendTotal;
    private double recSendRate;
    private double batchSizeMax;
    private double recSizeMax;
    private double recSizeAvg;
    private double recPerReqAvg;
    private double recQueTimeAvg;
    private double recQueTimeMax;



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

    public ResBuilder setResultsFromProducerMetricsMethod(Map<String, Double> metrics) {
        this.reqLatAvg = metrics.get("request-latency-avg");
        this.reqLatMax = metrics.get("request-latency-max");
        this.batchSizeAvg = metrics.get("batch-size-avg");
        this.recSendTotal = metrics.get("record-send-total");
        this.recSendRate = metrics.get("record-send-rate");
        this.batchSizeMax = metrics.get("batch-size-max");
        this.recSizeMax = metrics.get("record-size-max");
        this.recSizeAvg = metrics.get("record-size-avg");
        this.recPerReqAvg = metrics.get("records-per-request-avg");
        this.recQueTimeAvg = metrics.get("record-queue-time-avg");
        this.recQueTimeMax = metrics.get("record-queue-time-max");
        return this;
    }

    public ResBuilder setPeriodicalResultStrings(String producerRates, String producerThroughputs) {
        this.producerRates = producerRates;
        this.producerThroughputs = producerThroughputs;
        return this;
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

    public String getProducerRates() {
        return producerRates;
    }

    public String getProducerThroughputs() {
        return producerThroughputs;
    }

    public double getBatchSizeMax() {
        return batchSizeMax;
    }

    public double getRecQueTimeAvg() {
        return recQueTimeAvg;
    }

    public double getRecQueTimeMax() {
        return recQueTimeMax;
    }

    public double getRecSizeMax() {
        return recSizeMax;
    }

    public double getRecSizeAvg() {
        return recSizeAvg;
    }

    public double getRecPerReqAvg() {
        return recPerReqAvg;
    }
}
