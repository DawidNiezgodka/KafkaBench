package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.StringGenerator;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class KafkaBenchProducer implements Runnable {

    private static AtomicLong idCounter = new AtomicLong(0);
    private long id;

    private static final Logger LOGGER = LogManager.getLogger(KafkaBenchProducer.class);

    private final StringGenerator stringGenerator;
    private final KafkaProducer<String, byte[]> producer;
    private final String topic;

    private int producedMessages = 0;
    private List<Long> latencies = new ArrayList<>();

    public KafkaBenchProducer(KafkaProducer<String, byte[]> producer, String topic, StringGenerator stringGenerator) {
        this.producer = producer;
        this.topic = topic;
        this.stringGenerator = stringGenerator;
        this.id = idCounter.getAndIncrement();
    }

    @Override
    public void run() {
            sendMessage(stringGenerator.createPayloadRandomlyWithSize().getBytes(StandardCharsets.UTF_8));
            producedMessages++;
    }

    private void sendMessage(byte[] payload) {
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, payload);
        final long sendTime = System.nanoTime();
        producer.send(record);
        latencies.add(TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - sendTime));
    }

    public void close() {
        LOGGER.info("Thread {} produced {} messages.", id, producedMessages);
        this.producer.close();
    }

    public KafkaProducer<String, byte[]> getProducer() {
        return producer;
    }

    public int getProducedMessages() {
        return producedMessages;
    }

    public List<Long> getLatencies() {
        return latencies;
    }

    public long getAveLatency() {
        return (long) latencies.stream().mapToLong(l -> l).average().getAsDouble();
    }
}
