package backend.academy.statistics;

import backend.academy.model.LogRecord;
import backend.academy.statistics.metrics.BasicMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicMetricsTest {
    private BasicMetrics metrics;

    @BeforeEach
    void setUp() {
        metrics = new BasicMetrics();
    }

    @Test
    void testUpdate() {
        metrics.update(logBytesSize(500));
        metrics.update(logBytesSize(1000));

        assertEquals(2, metrics.totalRequests());
        assertEquals(750, metrics.averageResponseSize(), 0.01);
    }

    @Test
    void testPercentile50Calculation() {
        metrics.update(logBytesSize(500));
        metrics.update(logBytesSize(750));
        metrics.update(logBytesSize(1000));

        assertEquals(750, metrics.getPercentile(50), 0.01);
    }

    @Test
    void testPercentile95Calculation() {
        metrics.update(logBytesSize(100));
        metrics.update(logBytesSize(200));
        metrics.update(logBytesSize(300));
        metrics.update(logBytesSize(400));
        metrics.update(logBytesSize(500));

        assertEquals(480, metrics.getPercentile(95), 0.01);
    }

    private LogRecord logBytesSize(int size) {
        return new LogRecord(
            null,
            null,
            null,
            null,
            0,
            size,
            null,
            null
        );
    }
}
