package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.RequestMetrics;
import backend.academy.utils.Formatter;
import java.util.List;

public class RequestMethodsSection extends Section {
    @Override
    protected String getHeader() {
        return "Методы запросов";
    }

    @Override
    protected List<String> getTableHeaders() {
        return List.of("Метод", "Кол-во");
    }

    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        RequestMetrics metrics = statistics.getMetric(RequestMetrics.class);

        return metrics.getMethodFrequency().entrySet().stream()
            .map(entry -> List.of(entry.getKey(), Formatter.formatNum(entry.getValue())))
            .toList();
    }
}
