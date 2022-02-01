package kafka;

import metrics.ProducerStats;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.KeyGen;
import util.StringGenerator;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class KafkaBenchProducer implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(KafkaBenchProducer.class);
    private static final AtomicLong idCounter = new AtomicLong(0);

    private final long id;
    private final StringGenerator stringGenerator;
    private final KafkaProducer<String, byte[]> producer;
    private final String topic;
    private final ProducerStats producerStats;

    private boolean warmup = true;

    public KafkaBenchProducer(KafkaProducer<String, byte[]> producer, String topic, StringGenerator stringGenerator) {
        this.producer = producer;
        this.topic = topic;
        this.stringGenerator = stringGenerator;
        this.id = idCounter.getAndIncrement();
        this.producerStats = new ProducerStats();
    }

    @Override
    public void run() {
        if (!warmup) {
            sendMessage();
        } else {
            sendWarmupMessage();
        }
    }

    private void sendMessage() {
        byte[] payload = stringGenerator.createPayloadRandomlyWithSize().getBytes(StandardCharsets.UTF_8);
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, KeyGen.get(), payload);
        final long sendTime = System.nanoTime();
        producer.send(record, (recordMetadata, e) -> {
            if (e == null) {
                producerStats.periodicalMessageCount++;
                producerStats.periodicalByteCount += payload.length;
                producerStats.totalMessageCount++;
                producerStats.totalByteCount += payload.length;
                producerStats.latencies.add(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - sendTime));
            } else {
                LOGGER.info("Failed to send a payload: {}", payload);
                producerStats.periodicalFailedCount++;
                producerStats.totalFailedCount++;
            }
        });
    }

    private void sendWarmupMessage() {
        byte[] payload = stringGenerator.createPayloadRandomlyWithSize().getBytes(StandardCharsets.UTF_8);
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, payload);
        producer.send(record);
    }

    public void close() {
        this.producer.close();
    }

    public KafkaProducer<String, byte[]> getProducer() {
        return producer;
    }
    public ProducerStats getLocalStats() {
        return producerStats;
    }

    public void setWarmup(boolean warmup) {
        LOGGER.info("Terminating warmup mode...");
        this.warmup = warmup;
    }

}
