package backend.academy.filter;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogFilterTest {
    private LogFilter logFilter;

    @BeforeEach
    void setUp() {
        logFilter = new LogFilter();
    }

    @Test
    void testAddIncorrectFilters() {
        logFilter.addFilter("ADDRESS", LocalDateTime.of(2024, 11, 29, 15, 10));
        logFilter.addFilter("TO", "bredik");
        logFilter.addFilter("UNKNOWN", "value");

        assertTrue(logFilter.filters().isEmpty());
        assertTrue(logFilter.filterValues().isEmpty());
    }
}

