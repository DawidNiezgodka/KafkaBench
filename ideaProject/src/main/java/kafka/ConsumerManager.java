package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

public class ConsumerManager {

    private static final Logger LOGGER = LogManager.getLogger(ConsumerManager.class);

    private final Properties brokerProps = new Properties();
    private Properties consumerProps = new Properties();
    private final KafkaConfigWrapper config;

    public ConsumerManager(KafkaConfigWrapper config) {
        this.config = config;
        initConsumerProps();
    }

    private void initConsumerProps() {
        LOGGER.info("Initializing consumer properties...");
        try {
            brokerProps.load(new StringReader(config.getBrokerConfig()));
            consumerProps.putAll(brokerProps);
            consumerProps = new Properties();
            consumerProps.putAll(brokerProps);
            consumerProps.load(new StringReader(config.getConsumerConfig()));
            consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createConsumers(List<String> topics) {

    }

    public void ensureConsumersReadiness() {

    }
}
