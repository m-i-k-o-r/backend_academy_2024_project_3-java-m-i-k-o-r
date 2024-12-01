package backend.academy.filter;

import backend.academy.model.HttpRequest;
import backend.academy.model.LogRecord;
import java.util.Map;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogFilterStringDataTest {
    private LogFilter logFilter;

    @BeforeEach
    void setUp() {
        logFilter = new LogFilter();
    }

    @Test
    void testAddFilterStringAddressValue() {
        logFilter.addFilter("ADDRESS", "127.0.0.*");

        Map<FilterField, Predicate<LogRecord>> filters = logFilter.filters();
        assertTrue(filters.containsKey(FilterField.ADDRESS));

        LogRecord matchingRecord = createLogWithOnlyAddress("127.0.0.1");
        assertTrue(filters.get(FilterField.ADDRESS).test(matchingRecord));

        LogRecord nonMatchingRecord = createLogWithOnlyAddress("192.168.0.1");
        assertFalse(filters.get(FilterField.ADDRESS).test(nonMatchingRecord));
    }

    @Test
    void testAddFilterStringMethodValue() {
        logFilter.addFilter("METHOD", "POST");

        Map<FilterField, Predicate<LogRecord>> filters = logFilter.filters();
        assertTrue(filters.containsKey(FilterField.METHOD));

        LogRecord matchingRecord = createLogWithOnlyMethod("POST");
        assertTrue(filters.get(FilterField.METHOD).test(matchingRecord));

        LogRecord nonMatchingRecord = createLogWithOnlyMethod("GET");
        assertFalse(filters.get(FilterField.METHOD).test(nonMatchingRecord));
    }

    @Test
    void testAddFilterStringStatusValue() {
        logFilter.addFilter("STATUS", "200");

        Map<FilterField, Predicate<LogRecord>> filters = logFilter.filters();
        assertTrue(filters.containsKey(FilterField.STATUS));

        LogRecord matchingRecord = createLogWithOnlyStatus(200);
        assertTrue(filters.get(FilterField.STATUS).test(matchingRecord));

        LogRecord nonMatchingRecord = createLogWithOnlyStatus(404);
        assertFalse(filters.get(FilterField.STATUS).test(nonMatchingRecord));
    }

    @Test
    void testAddFilterStringAgentValue() {
        logFilter.addFilter("AGENT", "Debian *");

        Map<FilterField, Predicate<LogRecord>> filters = logFilter.filters();
        assertTrue(filters.containsKey(FilterField.AGENT));

        LogRecord matchingRecord = createLogWithOnlyAgent("Debian APT-HTTP/1.3 (0.9.7.9)");
        assertTrue(filters.get(FilterField.AGENT).test(matchingRecord));

        LogRecord nonMatchingRecord = createLogWithOnlyAgent("urlgrabber/3.9.1 yum/3.4.3");
        assertFalse(filters.get(FilterField.AGENT).test(nonMatchingRecord));
    }

    @Test
    void testAddSameFilters() {
        logFilter.addFilter("METHOD", "POST");
        logFilter.addFilter("METHOD", "GET");

        LogRecord recordPost = createLogWithOnlyMethod("POST");
        assertTrue(logFilter.filters().get(FilterField.METHOD).test(recordPost));

        LogRecord recordGet = createLogWithOnlyMethod("GET");
        assertTrue(logFilter.filters().get(FilterField.METHOD).test(recordGet));

        LogRecord recordDelete = createLogWithOnlyMethod("DELETE");
        assertFalse(logFilter.filters().get(FilterField.METHOD).test(recordDelete));
    }

    private LogRecord createLogWithOnlyAddress(String address) {
        return new LogRecord(
            address,
            null,
            null,
            null,
            0,
            0,
            null,
            null
        );
    }

    private LogRecord createLogWithOnlyMethod(String method) {
        return new LogRecord(
            null,
            null,
            null,
            new HttpRequest(method, null, null),
            0,
            0,
            null,
            null
        );
    }

    private LogRecord createLogWithOnlyStatus(int status) {
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

    private LogRecord createLogWithOnlyAgent(String agent) {
        return new LogRecord(
            null,
            null,
            null,
            null,
            0,
            0,
            null,
            agent
        );
    }
}
