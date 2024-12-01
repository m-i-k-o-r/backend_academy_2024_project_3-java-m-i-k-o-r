package backend.academy.statistics;

import backend.academy.filter.LogFilter;
import backend.academy.model.HttpRequest;
import backend.academy.model.LogRecord;
import backend.academy.statistics.metrics.BasicMetrics;
import backend.academy.statistics.metrics.Metric;
import backend.academy.statistics.metrics.RequestMetrics;
import backend.academy.statistics.metrics.ResponseMetric;
import backend.academy.statistics.metrics.TimeMetric;
import backend.academy.statistics.metrics.UserActivityMetric;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StatisticsTest {
    String source = "same source";
    LogStatistics statistics;

    @BeforeEach
    void setUp() {
        statistics = new LogStatistics(new LogFilter(), source);
    }

    @Test
    void testInitialization() {
        assertEquals(source, statistics.source());

        assertEquals(0, statistics.filters().filters().size());

        assertNotNull(statistics.getMetric(BasicMetrics.class));
        assertNotNull(statistics.getMetric(ResponseMetric.class));
        assertNotNull(statistics.getMetric(RequestMetrics.class));
        assertNotNull(statistics.getMetric(TimeMetric.class));
        assertNotNull(statistics.getMetric(UserActivityMetric.class));
    }

    @Test
    void testLogFiltering() {
        LogFilter filter = new LogFilter();
        filter.addFilter("STATUS", "200");

        LogStatistics statFiltered = new LogStatistics(filter, source);

        LogRecord filteredLog = new LogRecord(
            "1.0.0.1", "", LocalDateTime.of(2024, 11, 30, 15, 10),
            new HttpRequest("GET", "/downloads/product_1", "HTTP/1.1"),
            200, 2578, "", "Debian APT-HTTP/1.3 (0.9.7.9)"
        );

        LogRecord acceptedLog = new LogRecord(
            "1.0.0.1", "", LocalDateTime.of(2024, 11, 30, 15, 10),
            new HttpRequest("GET", "/downloads/product_1", "HTTP/1.1"),
            404, 2578, "", "Debian APT-HTTP/1.3 (0.9.7.9)"
        );

        statFiltered.update(filteredLog);
        statFiltered.update(acceptedLog);

        BasicMetrics basicMetrics = statFiltered.getMetric(BasicMetrics.class);
        assertEquals(1, basicMetrics.totalRequests());
    }

    @Test
    void testMetricsIntegration() {
        LogRecord log = new LogRecord(
            "1.0.0.1", "", LocalDateTime.of(2024, 11, 30, 15, 10),
            new HttpRequest("GET", "/downloads/product_1", "HTTP/1.1"),
            200, 2578, "", "Debian APT-HTTP/1.3 (0.9.7.9)"
        );

        statistics.update(log);

        BasicMetrics basicMetrics = statistics.getMetric(BasicMetrics.class);
        assertEquals(1, basicMetrics.totalRequests());

        ResponseMetric responseMetric = statistics.getMetric(ResponseMetric.class);
        assertEquals(1, responseMetric.getStatusCodeFrequency().get(200));

        RequestMetrics requestMetrics = statistics.getMetric(RequestMetrics.class);
        assertEquals(1, requestMetrics.getMostRequestedPaths(1).get("/downloads/product_1"));

        TimeMetric timeMetric = statistics.getMetric(TimeMetric.class);
        assertEquals(1, timeMetric.getHourlyRequestCountByDate(LocalDate.of(2024, 11, 30))
            .get(LocalDateTime.of(2024, 11, 30, 15, 0)));

        UserActivityMetric userActivityMetric = statistics.getMetric(UserActivityMetric.class);
        assertEquals(1, userActivityMetric.getTopActiveUsers(1).get("1.0.0.1"));
    }

    @Test
    void testGetNonExistMetric() {
        Metric nonExistentMetric = statistics.getMetric(Metric.class);
        assertNull(nonExistentMetric);
    }
}
