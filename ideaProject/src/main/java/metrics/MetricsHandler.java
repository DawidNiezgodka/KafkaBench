package metrics;

import java.util.List;

public class MetricsHandler {

    private List<ProducerMetrics> producerMetricsList;
    private int totalExpectedRecordCount;

    public MetricsHandler(List<ProducerMetrics> producerMetricsList, int totalExpectedRecordCount) {
        this.producerMetricsList = producerMetricsList;
        this.totalExpectedRecordCount = totalExpectedRecordCount;
    }

    public void writeMetrics() {

    }
}
