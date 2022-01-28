package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.StringGenerator;
import workload.WorkloadGenerator;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Create producers
 */
public class ProducerManager {

    private static final Logger LOGGER = LogManager.getLogger(ProducerManager.class);
    private final KafkaConfigWrapper config;
    private final StringGenerator stringGenerator;
    private final Properties brokerProps = new Properties();
    private final Properties producerProps = new Properties();

    public ProducerManager(KafkaConfigWrapper config, StringGenerator stringGenerator) {
        this.config = config;
        this.stringGenerator = stringGenerator;
        initProducerProps();
    }

    private void initProducerProps() {
        try {
            brokerProps.load(new StringReader(config.getBrokerConfig()));
            producerProps.putAll(brokerProps);
            producerProps.load(new StringReader(config.getProducerConfig()));
            producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KafkaBenchProducer createProducer(String topic) {
        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(producerProps);
        return new KafkaBenchProducer(producer, topic, stringGenerator);
    }

    public List<KafkaBenchProducer> createProducers(List<String> topics) {
        List<KafkaBenchProducer> producers = new ArrayList<>();
        topics.forEach(t -> producers.add(createProducer(t)));
        return producers;
    }
}
