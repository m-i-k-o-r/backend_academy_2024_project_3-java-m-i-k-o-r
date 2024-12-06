package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.TimeMetric;
import backend.academy.utils.Formatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static backend.academy.utils.Formatter.calculatePercentage;
import static backend.academy.utils.Formatter.formatNum;
import static backend.academy.utils.Formatter.generateBar;

/**
 * Класс для отображения секции с информацией о дневной активности
 * <hr>
 * Используется для генерации отчета, содержащего следующие данные:
 * <ul>
 *     <li>День с наибольшей активностью по количеству запросов</li>
 *     <li>Корреляцию между запросами и ошибками для самого активного дня</li>
 *     <li>Почасовое распределение запросов и ошибок для самого активного дня</li>
 * </ul>
 * Данные извлекаются из метрики {@link TimeMetric}
 */
public class MostActiveDaySection extends Section {
    @Override
    protected String getHeader() {
        return "Дневная активность";
    }

    /**
     * Подготавливает дополнительную информацию о самом активном дне
     * <hr>
     * Информация включает:
     * <ul>
     *     <li>
     *         дату дня с наибольшей активностью,
     *         полученную через {@link TimeMetric#getDayWithMostRequests()}
     *     </li>
     *     <li>
     *         корреляцию запросов и ошибок для данного дня,
     *         вычисленную с помощью {@link TimeMetric#calculateCorrelation(LocalDate)}
     *     </li>
     * </ul>
     * Если данных нет, возвращается пустой список
     */
    @Override
    protected List<List<String>> prepareInfo(LogStatistics statistics) {
        TimeMetric metric = statistics.getMetric(TimeMetric.class);
        LocalDate mostActiveDay = metric.getDayWithMostRequests();
        if (mostActiveDay == null) {
            return List.of();
        }

        return List.of(
            List.of("День с наибольшей активностью", mostActiveDay.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))),
            List.of("Корреляция", formatNum(metric.calculateCorrelation(mostActiveDay)))
        );
    }

    /**
     * Возвращает заголовки столбцов для таблицы секции.
     * <hr>
     * Таблица содержит следующие столбцы:
     * <ul>
     *     <li><b>Время</b> — часовой интервал</li>
     *     <li><b>Req</b> — количество запросов за данный час</li>
     *     <li><b>Request (chart)</b> — графическое представление запросов</li>
     *     <li><b>Err</b> — количество ошибок за данный час</li>
     *     <li><b>Error (chart)</b> — графическое представление ошибок</li>
     *     <li><b>Err (%)</b> — процент ошибок от общего количества запросов</li>
     * </ul>
     */
    @Override
    protected List<String> getTableHeaders() {
        return List.of("Время", "Req", "Request (chart)", "Err", "Error (chart)", "Err (%)");
    }

    /**
     * Подготавливает строки таблицы с почасовой статистикой запросов и ошибок для самого активного дня
     * <hr>
     * Строки таблицы включают:
     * <ul>
     *     <li>Временной интервал (час)</li>
     *     <li>
     *         Количество запросов и ошибок,
     *         полученные из {@link TimeMetric#getHourlyRequestCountByDate(LocalDate)}
     *         и {@link TimeMetric#getHourlyErrorCountByDate(LocalDate)}
     *     </li>
     *     <li>
     *         Графическое представление запросов и ошибок,
     *         созданное с помощью {@link Formatter#generateBar(int, int)}
     *     </li>
     *     <li>
     *         Процент ошибок,
     *         вычисленный с помощью {@link Formatter#calculatePercentage(int, int)}
     *     </li>
     * </ul>
     * <hr>
     * Если данных нет, возвращается строка-заглушка ("-", "-", "-", "-", "-", "-")
     */
    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        TimeMetric metric = statistics.getMetric(TimeMetric.class);

        LocalDate mostActiveDay = metric.getDayWithMostRequests();
        if (mostActiveDay == null) {
            return List.of(List.of("-", "-", "-", "-", "-", "-"));
        }

        Map<LocalDateTime, Integer> hourlyRequests = metric.getHourlyRequestCountByDate(mostActiveDay);
        Map<LocalDateTime, Integer> hourlyErrors = metric.getHourlyErrorCountByDate(mostActiveDay);

        int maxRequests = hourlyRequests.values().stream().max(Integer::compareTo).orElse(1);

        List<List<String>> rows = new ArrayList<>(hourlyRequests.size());
        for (LocalDateTime time : hourlyRequests.keySet()) {
            int requests = hourlyRequests.getOrDefault(time, 0);
            int errors = hourlyErrors.getOrDefault(time, 0);

            String requestBar = generateBar(requests, maxRequests);
            String errorBar = generateBar(errors, maxRequests);

            int errorPercent = calculatePercentage(errors, requests);

            rows.add(List.of(
                time.format(DateTimeFormatter.ofPattern("HH:mm")),
                String.valueOf(requests),
                requestBar,
                String.valueOf(errors),
                errorBar,
                String.format("%d%%", errorPercent)
            ));
        }

        return rows;
    }
}
