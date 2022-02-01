package workload;

/*
name: part=1,rate=10000
numberOfTopics: 4
partitionsPerTopic: 1
messageSize: 1024
producerRatePerSec: 30000
benchmarkDurationMinutes: 1
warmupDurationMinutes: 1
 */
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
