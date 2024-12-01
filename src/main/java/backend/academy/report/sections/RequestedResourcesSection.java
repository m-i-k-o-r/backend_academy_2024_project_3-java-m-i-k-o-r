package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.RequestMetrics;
import java.util.List;
import static backend.academy.utils.Constants.TOP_FIVE;
import static backend.academy.utils.Formatter.formatCode;
import static backend.academy.utils.Formatter.formatNum;

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

        List<List<String>> mostRequestedPaths = metrics.getMostRequestedPaths(TOP_FIVE).entrySet().stream()
            .map(entry -> List.of(formatCode(entry.getKey()), formatNum(entry.getValue())))
            .toList();

        if (mostRequestedPaths.isEmpty()) {
            return List.of(List.of("-", "-"));
        }

        return mostRequestedPaths;
    }
}
