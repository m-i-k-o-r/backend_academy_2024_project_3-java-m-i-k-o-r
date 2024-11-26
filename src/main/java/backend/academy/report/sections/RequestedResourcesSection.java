package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.RequestMetrics;
import backend.academy.utils.Formatter;
import java.util.List;
import static backend.academy.utils.Constants.TOP_FIVE;

public class RequestedResourcesSection extends Section {
    @Override
    protected String getHeader() {
        return "Запрашиваемые ресурсы";
    }

    @Override
    protected List<String> getTableHeaders() {
        return List.of("Ресурс", "Кол-во");
    }

    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        RequestMetrics metrics = statistics.getMetric(RequestMetrics.class);

        return metrics.getMostRequestedPaths(TOP_FIVE).entrySet().stream()
            .map(entry -> List.of(Formatter.formatCode(entry.getKey()), Formatter.formatNum(entry.getValue())))
            .toList();
    }
}
