package bench;

import metrics.ProducerStats;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PeriodicResult {

    private final LocalDateTime benchTime;
    private final String name;
    private final String producerRates;
    private final String producerThroughputs;

    public PeriodicResult(String name, List<Double> producerRates, List<Double> producerThroughputs) {
        this.benchTime = LocalDateTime.now();
        this.name = name;
        this.producerRates = producerRates.stream().map(Object::toString).collect(Collectors.joining(","));
        this.producerThroughputs = producerThroughputs.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public LocalDateTime getBenchTime() {
        return benchTime;
    }

    public String getName() {
        return name;
    }

    public String getProducerRates() {
        return producerRates;
    }

    public String getProducerThroughputs() {
        return producerThroughputs;
    }
}
