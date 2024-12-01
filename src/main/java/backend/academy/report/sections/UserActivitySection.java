package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.UserActivityMetric;
import java.util.List;
import static backend.academy.utils.Constants.TOP_FIVE;
import static backend.academy.utils.Formatter.formatCode;
import static backend.academy.utils.Formatter.formatNum;

public class UserActivitySection extends Section {
    @Override
    protected String getHeader() {
        return "Самые активные пользователи";
    }

    @Override
    protected List<List<String>> prepareInfo(LogStatistics statistics) {
        UserActivityMetric metric = statistics.getMetric(UserActivityMetric.class);
        return List.of(
            List.of("Кол-во уникальных пользователей", formatNum(metric.getCount()))
        );
    }

    @Override
    protected List<String> getTableHeaders() {
        return List.of("IP пользователя", "Количество запросов");
    }

    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        UserActivityMetric metrics = statistics.getMetric(UserActivityMetric.class);

        List<List<String>> mostActiveUsers = metrics.getTopActiveUsers(TOP_FIVE).entrySet().stream()
            .map(entry -> List.of(formatCode(entry.getKey()), formatNum(entry.getValue())))
            .toList();

        if (mostActiveUsers.isEmpty()) {
            return List.of(List.of("-", "-"));
        }

        return mostActiveUsers;
    }
}
