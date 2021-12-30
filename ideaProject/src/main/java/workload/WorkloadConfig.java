package workload;

public class WorkloadConfig {

    private String name;
    private int numberOfTopics;
    private int partitionsPerTopic;
    private int messageSize;
    private int consumersPerTopic;
    private int consumerBacklog;
    private int producersPerTopic;
    private int producerRatePerSec;
    private int benchmarkDurationMinutes;
    private int warmupDuration;
    private int sendingInterval;

    public String getName() {
        return name;
    }

    public int getNumberOfTopics() {
        return numberOfTopics;
    }

    public int getPartitionsPerTopic() {
        return partitionsPerTopic;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public int getConsumersPerTopic() {
        return consumersPerTopic;
    }

    public int getConsumerBacklog() {
        return consumerBacklog;
    }

    public int getProducersPerTopic() {
        return producersPerTopic;
    }

    public int getProducerRatePerSec() {
        return producerRatePerSec;
    }

    public int getBenchmarkDurationMinutes() {
        return benchmarkDurationMinutes;
    }

    public int getWarmupDuration() {
        return warmupDuration;
    }

    public int getSendingInterval() {
        return sendingInterval;
    }
}
