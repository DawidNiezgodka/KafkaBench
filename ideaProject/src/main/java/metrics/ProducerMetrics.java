package metrics;

import kafka.KafkaBenchProducer;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import util.BenchConsts;

import java.util.HashMap;
import java.util.Map;

public class ProducerMetrics {

    Map<MetricName, ? extends Metric> metrics;
    private final Map<String, Double> relevantMetrics;

    public ProducerMetrics(KafkaBenchProducer producer) {
        this.metrics = producer.getProducer().metrics();
        this.relevantMetrics = filterProducerMetrics(metrics);
    }

    private Map<String, Double> filterProducerMetrics(Map<MetricName,? extends Metric> metrics) {
        Map<String, Double> temp = new HashMap<>();
        String key = "";
        Double val = 0d;
        for (Map.Entry<MetricName,? extends Metric> entry : metrics.entrySet()) {
            key = entry.getKey().name();
            if (BenchConsts.RELEVANT_PRODUCER_METRICS.contains(key)&&
            entry.getKey().group().equals(BenchConsts.PRODUCER_METRICS)) {
                val = (Double) entry.getValue().metricValue();
                temp.put(key, temp.getOrDefault(key, 0d) + val);
            }
        }
        return temp;
    }

    public Map<String, Double> getRelevantMetrics() {
        return relevantMetrics;
    }
}
