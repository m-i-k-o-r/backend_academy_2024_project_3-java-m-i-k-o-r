package backend.academy.statistics;

import backend.academy.model.LogRecord;
import backend.academy.statistics.metrics.TimeMetric;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeMetricsTest {
    private TimeMetric metric;

    @BeforeEach
    void setUp() {
        metric = new TimeMetric();
    }

    @Test
    void testUpdateAndDailyRequestCount() {
        LocalDateTime baseTime = LocalDateTime.of(2024, 11, 30, 0, 0);

        metric.update(logStatusTime(200, baseTime.plusHours(15).plusMinutes(10)));
        metric.update(logStatusTime(500, baseTime.plusHours(15).plusMinutes(20)));
        metric.update(logStatusTime(404, baseTime.minusDays(1).plusHours(15).plusMinutes(10)));

        Map<LocalDateTime, Integer> map30_11 = metric.getHourlyRequestCountByDate(baseTime.toLocalDate());
        assertEquals(2, map30_11.get(baseTime.plusHours(15)));

        Map<LocalDateTime, Integer> map29_11 = metric.getHourlyRequestCountByDate(baseTime.toLocalDate().minusDays(1));
        assertEquals(1, map29_11.get(baseTime.minusDays(1).plusHours(15)));
    }

    @Test
    void testGetDayWithMostRequests() {
        LocalDateTime baseTime = LocalDateTime.of(2024, 11, 30, 0, 0);

        metric.update(logStatusTime(200, baseTime.plusHours(15).plusMinutes(10)));
        metric.update(logStatusTime(500, baseTime.plusHours(15).plusMinutes(20)));
        metric.update(logStatusTime(404, baseTime.minusDays(1).plusHours(15).plusMinutes(10)));

        assertEquals(baseTime.toLocalDate(), metric.getDayWithMostRequests());
    }

    @Test
    void testGetHourlyRequestCountByDate() {
        LocalDateTime baseTime = LocalDateTime.of(2024, 11, 30, 0, 0);

        metric.update(logStatusTime(200, baseTime.plusHours(15).plusMinutes(10)));
        metric.update(logStatusTime(500, baseTime.plusHours(15).plusMinutes(20)));
        metric.update(logStatusTime(404, baseTime.plusHours(9).plusMinutes(10)));

        Map<LocalDateTime, Integer> hourlyRequests = metric.getHourlyRequestCountByDate(baseTime.toLocalDate());
        assertEquals(2, hourlyRequests.get(baseTime.plusHours(15)));
        assertEquals(1, hourlyRequests.get(baseTime.plusHours(9)));
    }

    @Test
    void testCalculateCorrelation() {
        LocalDateTime baseTime = LocalDateTime.of(2024, 11, 30, 0, 0);

        metric.update(logStatusTime(500, baseTime.plusHours(15).plusMinutes(10)));
        metric.update(logStatusTime(200, baseTime.plusHours(15).plusMinutes(20)));
        metric.update(logStatusTime(404, baseTime.plusHours(15).plusMinutes(25)));
        metric.update(logStatusTime(500, baseTime.plusHours(9).plusMinutes(10)));

        double correlation = metric.calculateCorrelation(baseTime.toLocalDate());
        assertTrue(correlation <= 1 && correlation >= -1);
    }

    @Test
    void testUpdateWithStatusOnly() {
        LocalDateTime baseTime = LocalDateTime.of(2024, 11, 30, 0, 0);

        metric.update(logStatusTime(404, baseTime.plusHours(10).plusMinutes(15)));
        metric.update(logStatusTime(500, baseTime.plusHours(11).plusMinutes(45)));
        metric.update(logStatusTime(200, baseTime.plusHours(12).plusMinutes(30)));

        Map<LocalDateTime, Integer> errorCount = metric.getHourlyErrorCountByDate(baseTime.toLocalDate());

        assertEquals(1, errorCount.get(baseTime.plusHours(10)));
        assertEquals(1, errorCount.get(baseTime.plusHours(11)));
        assertEquals(0, errorCount.get(baseTime.plusHours(12)));
    }

    private LogRecord logStatusTime(int status, LocalDateTime time) {
        return new LogRecord(
            null,
            null,
            time,
            null,
            status,
            0,
            null,
            null
        );
    }
}
