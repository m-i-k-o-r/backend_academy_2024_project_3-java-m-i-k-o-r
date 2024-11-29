package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.RequestMetrics;
import java.util.List;
import static backend.academy.utils.Formatter.formatNum;

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
            .map(entry -> List.of(entry.getKey(), formatNum(entry.getValue())))
            .toList();
    }
}
