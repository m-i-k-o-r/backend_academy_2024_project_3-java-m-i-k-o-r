package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.RequestMetrics;
import java.util.List;
import static backend.academy.utils.Constants.TOP_FIVE;
import static backend.academy.utils.Formatter.formatCode;
import static backend.academy.utils.Formatter.formatNum;

/**
 * Класс для формирования секции с самыми запрашиваемыми ресурсами
 * <hr>
 * Используется для генерации отчета, показывающего, какие ресурсы
 * пользователи запрашивали чаще всего
 * <br>
 * Данные извлекаются из метрики {@link RequestMetrics}
 */
public class RequestedResourcesSection extends Section {
    @Override
    protected String getHeader() {
        return "Запрашиваемые ресурсы";
    }

    /**
     * Возвращает заголовки столбцов таблицы секции
     * <hr>
     * Таблица содержит следующие столбцы:
     * <ul>
     *     <li><b>ресурс</b> - путь запрашиваемого ресурса</li>
     *     <li><b>кол-во</b> - число запросов к данному ресурсу</li>
     * </ul>
     */
    @Override
    protected List<String> getTableHeaders() {
        return List.of("Ресурс", "Кол-во");
    }

    /**
     * Подготавливает строки таблицы с информацией о часто запрашиваемых ресурсах
     * <hr>
     * Информация формируется на основе топ-5 самых популярных путей ресурсов,
     * полученных из {@link RequestMetrics#getMostRequestedPaths(int)}
     * <hr>
     * Если данных нет, возвращается строка-заглушка ("-", "-")
     */
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
