package backend.academy.statistics;

import backend.academy.model.LogRecord;
import backend.academy.statistics.metrics.UserActivityMetric;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserActivityMetricTest {
    private UserActivityMetric metric;

    @BeforeEach
    void setUp() {
        metric = new UserActivityMetric();
    }

    @Test
    void testUpdateAndGetCount() {
        metric.update(logUserAddr("1.0.0.1"));
        metric.update(logUserAddr("1.0.0.2"));
        metric.update(logUserAddr("1.0.0.1"));
        metric.update(logUserAddr("1.0.0.1"));

        assertEquals(2, metric.getCount());
    }

    @Test
    void testUpdateAndGetTopActiveUsers() {
        metric.update(logUserAddr("1.0.0.1"));
        metric.update(logUserAddr("1.0.0.2"));
        metric.update(logUserAddr("1.0.0.1"));
        metric.update(logUserAddr("1.0.0.3"));
        metric.update(logUserAddr("1.0.0.1"));

        Map<String, Integer> topUsers = metric.getTopActiveUsers(5);
        assertEquals(3, topUsers.size());

        assertEquals(3, topUsers.get("1.0.0.1"));
        assertEquals(1, topUsers.get("1.0.0.2"));

        assertEquals("1.0.0.1", topUsers.keySet().iterator().next());
    }

    private LogRecord logUserAddr(String userAddress) {
        return new LogRecord(
            userAddress,
            null,
            null,
            null,
            0,
            0,
            null,
            null
        );
    }
}
