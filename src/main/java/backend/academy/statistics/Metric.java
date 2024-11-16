package backend.academy.statistics;

import backend.academy.parser.LogRecord;

public interface Metric {
    void update(LogRecord log);
}
