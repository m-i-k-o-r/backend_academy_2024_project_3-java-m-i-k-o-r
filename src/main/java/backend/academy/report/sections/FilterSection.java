package backend.academy.report.sections;

import backend.academy.filter.FilterField;
import backend.academy.filter.LogFilter;
import backend.academy.statistics.LogStatistics;
import java.util.List;

public class FilterSection extends Section {
    @Override
    protected String getHeader() {
        return "Фильтры";
    }

    @Override
    protected List<String> getTableHeaders() {
        return List.of("Название", "Значение");
    }

    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        LogFilter logFilter = statistics.filters();

        if (logFilter.filterValues().isEmpty()) {
            return List.of(List.of("-", "-"));
        }

        return logFilter.filterValues().entrySet().stream()
            .map(entry -> {
                FilterField field = entry.getKey();
                String filterValue = entry.getValue();

                return List.of(field.label(), filterValue);
            })
            .toList();
    }
}
