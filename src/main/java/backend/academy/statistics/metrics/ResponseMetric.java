package backend.academy.statistics.metrics;

import backend.academy.model.LogRecord;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ResponseMetric implements Metric {
    private final Map<Integer, Integer> statusCodeFrequency = new HashMap<>();

    @Override
    public void update(LogRecord log) {
        statusCodeFrequency.merge(log.status(), 1, Integer::sum);
    }

    public Map<Integer, Integer> getStatusCodeFrequency() {
        return statusCodeFrequency.entrySet()
            .stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ));
    }
}
