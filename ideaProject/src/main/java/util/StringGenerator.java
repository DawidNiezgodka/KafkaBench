package util;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class StringGenerator {

    private static final String NOT_LEGAL_CHARS_IN_KAFKA_TOPIC = "[^a-zA-Z0-9\\._\\-]";

    private final Random random;
    private int defaultMessageSize;

    public StringGenerator() {
        this.random = new Random();
    }

    public StringGenerator(int defaultMessageSize) {
        this.random = new Random();
        this.defaultMessageSize = defaultMessageSize;
    }

    public String createPayloadRandomly(int payloadSize) {
        byte[] array = new byte[payloadSize];
        this.random.nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public String createRandomTopicName(int topicNameLen) {
        byte[] array = new byte[topicNameLen];
        this.random.nextBytes(array);
        String topicName =  new String(array, StandardCharsets.UTF_8);
        return topicName.replaceAll(NOT_LEGAL_CHARS_IN_KAFKA_TOPIC, "");
    }

    public String getDefaultPayload() {
        return BenchConsts.STRING_1024;
    }
}
