package backend.academy.manager;

import backend.academy.cli.CliParams;
import backend.academy.filter.FilterField;
import backend.academy.filter.LogFilter;
import java.time.LocalDateTime;
import java.util.List;

public class FilterManager {
    public static LogFilter configureFilters(CliParams params) {
        LogFilter filters = new LogFilter();

        List<String> fields = params.filterFields();
        List<String> values = params.filterValues();

        if (fields != null && values != null) {
            for (int i = 0; i < fields.size(); i++) {
                filters.addFilter(fields.get(i), values.get(i));
            }
        }

        addDateFilter(filters, FilterField.TO, params.to());
        addDateFilter(filters, FilterField.FROM, params.from());

        return filters;
    }

    private static void addDateFilter(LogFilter filters, FilterField field, LocalDateTime date) {
        if (date != null) {
            filters.addFilter(String.valueOf(field), date);
        }
    }
}
