package backend.academy.statistics.metrics;

import backend.academy.model.LogRecord;

public interface Metric {
    void update(LogRecord log);
}
