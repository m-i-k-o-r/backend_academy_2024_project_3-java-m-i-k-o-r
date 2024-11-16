package backend.academy.statistics.metrics;

import backend.academy.model.LogRecord;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestMetrics implements Metric {
    private final Map<String, Integer> pathFrequency = new HashMap<>();
    private final Map<String, Integer> methodFrequency = new HashMap<>();

    @Override
    public void update(LogRecord entry) {
        pathFrequency.merge(entry.request().path(), 1, Integer::sum);
        methodFrequency.merge(entry.request().method(), 1, Integer::sum);
    }

    public Map<String, Integer> getMostRequestedPaths(int limit) {
        return pathFrequency.entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ));
    }

    public Map<String, Integer> getMethodFrequency() {
        return methodFrequency;
    }
}
