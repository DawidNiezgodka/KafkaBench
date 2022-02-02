package workload;

public class WorkloadConfig {

    private String name;
    private int numberOfTopics;
    private int partitionsPerTopic;
    private int messageSize;
    private int producerRatePerSec;
    private int benchmarkDurationMinutes;
    private int warmupDurationMinutes;

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

    public int getProducerRatePerSec() {
        return producerRatePerSec;
    }

    public int getBenchmarkDurationMinutes() {
        return benchmarkDurationMinutes;
    }

    public int getWarmupDurationMinutes() {
        return warmupDurationMinutes;
    }
}
