package kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.internals.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class TopicManager {

    private static final Logger LOGGER = LogManager.getLogger(TopicManager.class);

    private final ConfigWrapper config;
    private AdminClient adminClient;
    private final Properties brokerProps = new Properties();
    private final Properties topicProps = new Properties();


    public TopicManager(ConfigWrapper configWrapper) {
        this.config = configWrapper;
        initTopicPropsAndAdminClient();
    }

    private void initTopicPropsAndAdminClient() {
        try {
            brokerProps.load(new StringReader(config.getBrokerConfig()));
            topicProps.load(new StringReader(config.getTopicConfig()));
            adminClient = AdminClient.create(brokerProps);
            // Delete all topics from previous runs
            ListTopicsResult topics = adminClient.listTopics();
            Set<String> topicNames = topics.names().get();
            DeleteTopicsResult deletedTopics = adminClient.deleteTopics(topicNames);
            deletedTopics.all().get();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void createTopic(String topicName, int numberOfPartitions) {
        try {
            NewTopic newTopic = new NewTopic(topicName, numberOfPartitions, (short) config.getReplicationFactor());
            newTopic.configs(new HashMap<>((Map) topicProps));
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getAllTopics() {
        Set<String> allTopics = new HashSet<>();
        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);
        try {
            allTopics = adminClient.listTopics(listTopicsOptions).names().get();
            allTopics.forEach(t -> LOGGER.info("Found topic named {}", t));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return allTopics;
    }

    public void deleteAllTopics() {
        try {
            Set<String> existingTopics = getAllTopics();
            LOGGER.info("Trying to delete {} topics", existingTopics.size());
            DeleteTopicsResult deletes = adminClient.deleteTopics(existingTopics);
            deletes.all().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void closeAdmin() {
        this.adminClient.close();
    }


}
