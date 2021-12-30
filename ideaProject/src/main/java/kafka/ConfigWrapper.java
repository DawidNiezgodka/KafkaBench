package kafka;

public class ConfigWrapper {

    private int replicationFactor;
    private int messageSize;
    private String brokerConfig;
    private String topicConfig;
    private String producerConfig;
    private String consumerConfig;

    public int getReplicationFactor() {
        return replicationFactor;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public String getBrokerConfig() {
        return brokerConfig;
    }

    public String getTopicConfig() {
        return topicConfig;
    }

    public String getProducerConfig() {
        return producerConfig;
    }

    public String getConsumerConfig() {
        return consumerConfig;
    }
}