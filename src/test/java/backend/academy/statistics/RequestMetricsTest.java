package backend.academy.statistics;

import backend.academy.model.HttpRequest;
import backend.academy.model.LogRecord;
import backend.academy.statistics.metrics.RequestMetrics;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestMetricsTest {
    private RequestMetrics metrics;

    @BeforeEach
    void setUp() {
        metrics = new RequestMetrics();
    }

    @Test
    void testUpdate() {
        metrics.update(logMethodPath("GET", "/path/1"));
        metrics.update(logMethodPath("POST", "/path/new/2"));
        metrics.update(logMethodPath("GET", "/path/1"));
        metrics.update(logMethodPath("DELETE", "/path/1"));

        Map<String, Integer> pathFrequency = metrics.getMostRequestedPaths(10);
        assertEquals(3, pathFrequency.get("/path/1"));
        assertEquals(1, pathFrequency.get("/path/new/2"));

        Map<String, Integer> methodFrequency = metrics.getMethodFrequency();
        assertEquals(2, methodFrequency.get("GET"));
        assertEquals(1, methodFrequency.get("POST"));
        assertEquals(1, methodFrequency.get("DELETE"));
    }

    @Test
    void testGetMostRequestedPaths() {
        metrics.update(logMethodPath("GET", "/path/1"));
        metrics.update(logMethodPath("GET", "/path/1"));
        metrics.update(logMethodPath("GET", "/path/new/2"));

        Map<String, Integer> topPaths = metrics.getMostRequestedPaths(10);
        assertEquals(2, topPaths.size());
        assertTrue(topPaths.containsKey("/path/1"));
        assertEquals(2, topPaths.get("/path/1"));
    }

    @Test
    void testGetMethodFrequency() {
        metrics.update(logMethodPath("GET", "/path/1"));
        metrics.update(logMethodPath("POST", "/path/new/2"));
        metrics.update(logMethodPath("UPDATE", "/path/1"));
        metrics.update(logMethodPath("GET", "/path/1"));

        Map<String, Integer> methodFrequency = metrics.getMethodFrequency();
        assertEquals(3, methodFrequency.size());
        assertEquals(2, methodFrequency.get("GET"));
        assertEquals(1, methodFrequency.get("POST"));
        assertEquals(1, methodFrequency.get("UPDATE"));
    }

    private LogRecord logMethodPath(String method, String path) {
        return new LogRecord(
            null,
            null,
            null,
            new HttpRequest(method, path, null),
            0,
            0,
            null,
            null
        );
    }
}
