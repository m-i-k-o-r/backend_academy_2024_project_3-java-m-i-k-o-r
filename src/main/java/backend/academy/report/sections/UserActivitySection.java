package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.UserActivityMetric;
import java.util.List;
import static backend.academy.utils.Constants.TOP_FIVE;
import static backend.academy.utils.Formatter.formatCode;
import static backend.academy.utils.Formatter.formatNum;

/**
 * Класс для формирования секции с информацией о самых активных пользователях
 * <hr>
 * Используется для генерации отчета, содержащего таблицу с IP-адресами пользователей
 * и количеством их запросов, а также дополнительную информацию об активности
 * <br>
 * Данные извлекаются из метрики {@link UserActivityMetric}
 */
public class UserActivitySection extends Section {
    @Override
    protected String getHeader() {
        return "Самые активные пользователи";
    }

    /**
     * Подготавливает дополнительную информацию об активности пользователей
     * <br>
     * Включает общее количество уникальных пользователей,
     * извлеченные из {@link UserActivityMetric#getCount()}.
     */
    @Override
    protected List<List<String>> prepareInfo(LogStatistics statistics) {
        UserActivityMetric metric = statistics.getMetric(UserActivityMetric.class);
        return List.of(
            List.of("Кол-во уникальных пользователей", formatNum(metric.getCount()))
        );
    }

    /**
     * Возвращает заголовки столбцов таблицы секции
     * <hr>
     * Таблица содержит следующие столбцы:
     * <ul>
     *     <li><b>IP пользователя</b></li>
     *     <li><b>количество запросов</b></li>
     * </ul>
     */
    @Override
    protected List<String> getTableHeaders() {
        return List.of("IP пользователя", "Количество запросов");
    }

    /**
     * Подготавливает строки таблицы с данными о самых активных пользователях
     * <hr>
     * Информация формируется на основе топ-5 пользователей по количеству запросов,
     * полученных из {@link UserActivityMetric#getTopActiveUsers(int)}
     * <hr>
     * Если данных нет, возвращается строка-заглушка ("-", "-")
     */
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
