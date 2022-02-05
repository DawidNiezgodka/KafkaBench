package bench;

import java.time.LocalDateTime;

public class BenchmarkResult {

    private final String benchTime;
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
    private final double aveLatency;
    private final double maxLatency;
    private final String producerRates;
    private final String producerThroughputs;
    private final long failedMessages;
    private final long sentMessages;

    /**
     * Metrics from the KafkaProducer's metrics() method
     */
    private final double reqLatAvg;
    private final double reqLatMax;
    private final double batchSizeAvg;
    private final double recSendTotal;
    private final double recSendRate;
    private final double batchSizeMax;
    private final double recSizeMax;
    private final double recSizeAvg;
    private final double recPerReqAvg;
    private final double recQueTimeAvg;
    private final double recQueTimeMax;

    public BenchmarkResult(ResBuilder resBuilder) {
        this.benchTime = LocalDateTime.now().toString();
        this.name = resBuilder.getName();
        this.publishRate = resBuilder.getPublishRate();
        this.partitions = resBuilder.getPartitions();
        this.acks = resBuilder.getAcks();
        this.batchSize = resBuilder.getBatchSize();
        this.lingerMs = resBuilder.getLingerMs();
        this.aveLatency = resBuilder.getAveLatency();
        this.maxLatency = resBuilder.getMaxLatency();
        this.sentMessages = resBuilder.getSentMessages();
        this.failedMessages = resBuilder.getFailedMessages();
        this.reqLatAvg = resBuilder.getReqLatAvg();
        this.reqLatMax = resBuilder.getReqLatMax();
        this.batchSizeAvg = resBuilder.getBatchSizeAvg();
        this.recSendTotal = resBuilder.getRecSendTotal();
        this.recSendRate = resBuilder.getRecSendRate();
        this.producerRates = resBuilder.getProducerRates();
        this.producerThroughputs = resBuilder.getProducerThroughputs();
        this.batchSizeMax = resBuilder.getBatchSizeMax();
        this.recSizeMax = resBuilder.getRecSizeMax();
        this.recSizeAvg = resBuilder.getRecSizeAvg();
        this.recPerReqAvg = resBuilder.getRecPerReqAvg();
        this.recQueTimeAvg = resBuilder.getRecQueTimeAvg();
        this.recQueTimeMax = resBuilder.getRecQueTimeMax();


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

    public String getBenchTime() {
        return benchTime;
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

    public long getFailedMessages() {
        return failedMessages;
    }

    public long getSentMessages() {
        return sentMessages;
    }
}
