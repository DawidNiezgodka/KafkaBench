package metrics;

import java.util.List;

public class MetricsHandler {

    private List<ProducerMetrics> producerMetricsList;
    private List<ProducerStats> producerStatsList;
    private long totalExpectedRecordCount;

    public MetricsHandler(List<ProducerMetrics> producerMetricsList,
                          List<ProducerStats> producerStatsList, long totalExpectedRecordCount) {
        this.producerMetricsList = producerMetricsList;
        this.producerStatsList = producerStatsList;
        this.totalExpectedRecordCount = totalExpectedRecordCount;
    }

    public void writeMetrics() {

    }
}
