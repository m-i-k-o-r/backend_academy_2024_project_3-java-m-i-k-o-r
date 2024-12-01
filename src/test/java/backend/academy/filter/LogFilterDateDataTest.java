package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogFilterDateDataTest {
    private LogFilter logFilter;

    @BeforeEach
    void setUp() {
        logFilter = new LogFilter();
    }

    @Test
    void testAddFilterFromDate() {
        LocalDateTime filterDate = LocalDateTime.of(2024, 11, 29, 15, 10);
        logFilter.addFilter("FROM", filterDate);

        Map<FilterField, Predicate<LogRecord>> filters = logFilter.filters();
        assertTrue(filters.containsKey(FilterField.FROM));

        LogRecord matchingRecord = createLogWithOnlyDate(LocalDateTime.of(2024, 11, 30, 0, 0));
        assertTrue(filters.get(FilterField.FROM).test(matchingRecord));

        LogRecord nonMatchingRecord = createLogWithOnlyDate(LocalDateTime.of(2021, 11, 29, 15, 10));
        assertFalse(filters.get(FilterField.FROM).test(nonMatchingRecord));
    }

    @Test
    void testAddFilterToDate() {
        LocalDateTime filterDate = LocalDateTime.of(2024, 11, 29, 15, 10);
        logFilter.addFilter("TO", filterDate);

        Map<FilterField, Predicate<LogRecord>> filters = logFilter.filters();
        assertTrue(filters.containsKey(FilterField.TO));

        LogRecord matchingRecord = createLogWithOnlyDate(LocalDateTime.of(2021, 11, 29, 15, 10));
        assertTrue(filters.get(FilterField.TO).test(matchingRecord));

        LogRecord nonMatchingRecord = createLogWithOnlyDate(LocalDateTime.of(2024, 11, 30, 0, 0));
        assertFalse(filters.get(FilterField.TO).test(nonMatchingRecord));
    }

    @Test
    void testAddFilterFromAndToDate() {
        LocalDateTime fromDate = LocalDateTime.of(2024, 11, 28, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2024, 11, 30, 0, 0);

        logFilter.addFilter("FROM", fromDate);
        logFilter.addFilter("TO", toDate);

        Map<FilterField, Predicate<LogRecord>> filters = logFilter.filters();
        Predicate<LogRecord> dateRangeFilter = filters.get(FilterField.FROM).and(filters.get(FilterField.TO));

        LogRecord matchingRecord = createLogWithOnlyDate(LocalDateTime.of(2024, 11, 29, 12, 0));
        assertTrue(dateRangeFilter.test(matchingRecord));

        LogRecord earlyRecord = createLogWithOnlyDate(LocalDateTime.of(2024, 11, 27, 23, 59));
        assertFalse(dateRangeFilter.test(earlyRecord));

        LogRecord lateRecord = createLogWithOnlyDate(LocalDateTime.of(2024, 12, 1, 0, 0));
        assertFalse(dateRangeFilter.test(lateRecord));
    }

    private LogRecord createLogWithOnlyDate(LocalDateTime date) {
        return new LogRecord(
            null,
            null,
            date,
            null,
            0,
            0,
            null,
            null
        );
    }
}
