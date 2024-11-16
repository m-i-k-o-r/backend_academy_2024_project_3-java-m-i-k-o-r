package backend.academy.statistics;

import backend.academy.filter.LogFilter;
import backend.academy.parser.LogRecord;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public class LogStatistics {
    @Getter
    private String source = "not name";
    private final Map<Class<? extends Metric>, Metric> metrics = new HashMap<>();
    private final LogFilter filters;

    public LogStatistics(LogFilter filters, String source) {
        this.filters = filters;
        this.source = source;

        metrics.put(BasicMetrics.class, new BasicMetrics());
        metrics.put(ResponseMetric.class, new ResponseMetric());
        metrics.put(RequestMetrics.class, new RequestMetrics());
        metrics.put(TimeMetric.class, new TimeMetric());
        metrics.put(UserActivityMetric.class, new UserActivityMetric());
    }

    public void update(LogRecord entry) {
        if (!filters.matches(entry)) {
            return;
        }
        for (Metric metric : metrics.values()) {
            metric.update(entry);
        }
    }
}
