package backend.academy.statistics;

import backend.academy.model.LogRecord;
import backend.academy.statistics.metrics.ResponseMetric;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseMetricTest {
    private ResponseMetric metric;

    @BeforeEach
    void setUp() {
        metric = new ResponseMetric();
    }

    @Test
    void testUpdate() {
        metric.update(logStatus(200));
        metric.update(logStatus(404));
        metric.update(logStatus(200));

        Map<Integer, Integer> statusCodeFrequency = metric.getStatusCodeFrequency();
        assertEquals(2, statusCodeFrequency.get(200));
        assertEquals(1, statusCodeFrequency.get(404));
        assertEquals(2, statusCodeFrequency.size());
    }

    @Test
    void testGetStatusCodeFrequencySorting() {
        metric.update(logStatus(500));
        metric.update(logStatus(404));
        metric.update(logStatus(500));
        metric.update(logStatus(500));
        metric.update(logStatus(404));
        metric.update(logStatus(200));

        Map<Integer, Integer> sortedStatusCodeFrequency = metric.getStatusCodeFrequency();
        Integer[] expectedOrder = {500, 404, 200};

        assertArrayEquals(
            expectedOrder,
            sortedStatusCodeFrequency.keySet().toArray()
        );

        assertEquals(3, sortedStatusCodeFrequency.get(500));
        assertEquals(2, sortedStatusCodeFrequency.get(404));
        assertEquals(1, sortedStatusCodeFrequency.get(200));
    }

    private LogRecord logStatus(int status) {
        return new LogRecord(
            null,
            null,
            null,
            null,
            status,
            0,
            null,
            null
        );
    }
}
