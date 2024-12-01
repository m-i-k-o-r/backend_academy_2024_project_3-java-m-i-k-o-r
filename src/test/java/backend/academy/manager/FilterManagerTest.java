package backend.academy.manager;

import backend.academy.cli.CliParams;
import backend.academy.filter.FilterField;
import backend.academy.filter.LogFilter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilterManagerTest {
    CliParams params = mock(CliParams.class);

    @Test
    void testConfigureFiltersWithFieldAndValueFilters() {
        when(params.filterFields()).thenReturn(List.of("method", "address"));
        when(params.filterValues()).thenReturn(List.of("value_1", "value_2"));
        when(params.to()).thenReturn(null);
        when(params.from()).thenReturn(null);

        LogFilter filters = FilterManager.configureFilters(params);

        assertEquals(2, filters.filters().size());
        assertEquals("value_1", filters.filterValues().get(FilterField.METHOD));
        assertEquals("value_2", filters.filterValues().get(FilterField.ADDRESS));
    }

    @Test
    void testConfigureFiltersWithDateFilters() {
        LocalDateTime fromDate = LocalDateTime.of(2024, 11, 30, 10, 0);
        LocalDateTime toDate = LocalDateTime.of(2024, 12, 1, 18, 0);

        when(params.filterFields()).thenReturn(null);
        when(params.filterValues()).thenReturn(null);
        when(params.from()).thenReturn(fromDate);
        when(params.to()).thenReturn(toDate);

        LogFilter filters = FilterManager.configureFilters(params);

        assertEquals(2, filters.filters().size());
        assertEquals(formatDate(fromDate), filters.filterValues().get(FilterField.FROM));
        assertEquals(formatDate(toDate), filters.filterValues().get(FilterField.TO));
    }

    @Test
    void testConfigureFiltersWithMixedFilters() {
        LocalDateTime fromDate = LocalDateTime.of(2024, 11, 30, 10, 0);

        when(params.filterFields()).thenReturn(List.of("agent"));
        when(params.filterValues()).thenReturn(List.of("value_1"));
        when(params.from()).thenReturn(fromDate);
        when(params.to()).thenReturn(null);

        LogFilter filters = FilterManager.configureFilters(params);

        assertEquals(2, filters.filters().size());
        assertEquals("value_1", filters.filterValues().get(FilterField.AGENT));
        assertEquals(formatDate(fromDate), filters.filterValues().get(FilterField.FROM));
    }

    @Test
    void testConfigureFiltersWithNoFilters() {
        when(params.filterFields()).thenReturn(null);
        when(params.filterValues()).thenReturn(null);
        when(params.to()).thenReturn(null);
        when(params.from()).thenReturn(null);

        LogFilter filters = FilterManager.configureFilters(params);

        assertTrue(filters.filters().isEmpty());
    }

    private String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"));
    }
}
