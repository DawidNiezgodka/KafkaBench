package kafka;

public class KafkaConfigWrapper {

    private String name;
    private int replicationFactor;
    private int messageSize;
    private String brokerConfig;
    private String topicConfig;
    private String producerConfig;

    public String getName() {
        return name;
    }

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
}
