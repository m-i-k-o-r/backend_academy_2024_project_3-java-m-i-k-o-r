package backend.academy.statistics.metrics;

import backend.academy.model.LogRecord;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UserActivityMetric implements Metric {
    private final Map<String, Integer> userRequestCount = new HashMap<>();

    @Override
    public void update(LogRecord log) {
        userRequestCount.merge(log.remoteAddr(), 1, Integer::sum);
    }

    public int getCount() {
        return userRequestCount.size();
    }

    public Map<String, Integer> getTopActiveUsers(int limit) {
        return userRequestCount.entrySet()
            .stream()
            .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
            .limit(limit)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ));
    }
}
