package backend.academy.statistics;

import lombok.Getter;

@Getter
public class LogStatistics {
    private int count = 0;

    public LogStatistics() {

    }

    public void update(LogRecord log) {
        count++;
    }
}

