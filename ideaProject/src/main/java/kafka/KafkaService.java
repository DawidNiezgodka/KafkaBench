package kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.BenchConsts;
import util.StringGenerator;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class KafkaService {

    private static final Logger LOGGER = LogManager.getLogger(KafkaService.class);
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    private KafkaConfigWrapper kafkaConfigWrapper;
    private String kafkaConfigName;

    private StringGenerator stringGenerator;

    private TopicManager topicManager;
    private ProducerManager producerManager;
    private ConsumerManager consumerManager;

    List<KafkaBenchProducer> producers = new ArrayList<>();
    List<KafkaBenchConsumer> consumers = new ArrayList<>();

    public KafkaService(File configurationFile) {

        init(configurationFile);
    }

    private void init(File configurationFile) {

        try {
            kafkaConfigWrapper = MAPPER.readValue(configurationFile, KafkaConfigWrapper.class);
            LOGGER.info("Read kafka config file: {}", kafkaConfigWrapper.toString());
            this.kafkaConfigName = kafkaConfigWrapper.getName();
            this.stringGenerator = new StringGenerator(kafkaConfigWrapper.getMessageSize());
            topicManager = new TopicManager(kafkaConfigWrapper);
            producerManager = new ProducerManager(kafkaConfigWrapper, stringGenerator);
            consumerManager = new ConsumerManager(kafkaConfigWrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        for (KafkaBenchConsumer consumer : consumers) {
            // consumer.close()
        }
        for (KafkaBenchProducer producer : producers) {
            producer.close();
        }
        topicManager.closeAdmin();
    }

    public Set<String> createTopics(int numberOfTopics, int partitionsPerTopic) {
        // First delete existing topics
        topicManager.deleteAllTopics();
        for (int i=0; i<numberOfTopics; i++) {
            String topicName = stringGenerator.createRandomTopicName(BenchConsts.DEFAULT_TOPIC_LEN);
            topicManager.createTopic(topicName, partitionsPerTopic);
        }
        Set<String> createdTopics = topicManager.getAllTopics();
        if (createdTopics.size() != numberOfTopics) {
            LOGGER.error("Something went wrong. Only {} out of {} topics were created",
                    createdTopics.size(), numberOfTopics);
            System.exit(-1);
        }
        return createdTopics;
    }

    public void createProducers(Set<String> topics) {
        for (String topic : topics) {
            producers.add(producerManager.createProducer(topic));
        }
    }

    public Map<String, String> getProducerProperties() {
        HashMap<String, String> propsMap = new HashMap<>();
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(kafkaConfigWrapper.getProducerConfig()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            propsMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return propsMap;
    }

    public String getKafkaConfigName() {return kafkaConfigName;}
    public List<KafkaBenchProducer> getProducers() {
        return producers;
    }
    public List<KafkaBenchConsumer> getConsumers() {
        return consumers;
    }
}
