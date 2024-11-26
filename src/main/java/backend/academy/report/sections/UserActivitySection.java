package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.UserActivityMetric;
import backend.academy.utils.Formatter;
import java.util.List;
import static backend.academy.utils.Constants.TOP_FIVE;

public class UserActivitySection extends Section {
    @Override
    protected String getHeader() {
        return "Самые активные пользователи";
    }

    @Override
    protected List<List<String>> prepareInfo(LogStatistics statistics) {
        UserActivityMetric metric = statistics.getMetric(UserActivityMetric.class);
        return List.of(
            List.of("Кол-во уникальных пользователей", Formatter.formatNum(metric.getCount()))
        );
    }

    @Override
    protected List<String> getTableHeaders() {
        return List.of("IP пользователя", "Количество запросов");
    }

    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        UserActivityMetric metrics = statistics.getMetric(UserActivityMetric.class);
        return metrics.getTopActiveUsers(TOP_FIVE).entrySet().stream()
            .map(entry -> List.of(Formatter.formatCode(entry.getKey()), Formatter.formatNum(entry.getValue())))
            .toList();
    }
}
