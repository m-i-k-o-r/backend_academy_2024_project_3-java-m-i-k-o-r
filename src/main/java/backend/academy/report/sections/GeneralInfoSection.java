package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.BasicMetrics;
import java.util.List;
import static backend.academy.utils.Constants.PERCENTILE_50;
import static backend.academy.utils.Constants.PERCENTILE_95;
import static backend.academy.utils.Formatter.formatLink;
import static backend.academy.utils.Formatter.formatNum;

/**
 * Класс для отображения секции с общей информацией
 * <hr>
 * Используется для генерации отчета, содержащего основные метрики и статистику,
 * такие как общее количество запросов, средний размер ответа,
 * перцентили размера ответа (95 и 50), а также информацию об источнике данных
 * <br>
 * Данные извлекаются из метрики {@link BasicMetrics}
 */
public class GeneralInfoSection extends Section {
    @Override
    protected String getHeader() {
        return "Общая информация";
    }

    /**
     * Возвращает заголовки столбцов таблицы секции
     * <hr>
     * Таблица содержит два столбца:
     * <ul>
     *     <li><b>метрика</b> - название метрики</li>
     *     <li><b>значение</b> - соответствующее значение метрики</li>
     * </ul>
     */
    @Override
    protected List<String> getTableHeaders() {
        return List.of("Метрика", "Значение");
    }

    /**
     * Подготавливает строки таблицы с общей информацией
     * <hr>
     * Строки содержат основные метрики, такие как:
     * <ul>
     *     <li><b>файл (источник)</b> полученный из {@link LogStatistics#source()}</li>
     *     <li><b>количество запросов</b> полученный из {@link BasicMetrics#totalRequests()}</li>
     *     <li><b>средний размер ответа</b> полученный из {@link BasicMetrics#averageResponseSize()}</li>
     *     <li>
     *         <b>перцентили размера ответа (95 и 50)</b>
     *         вычисленные с помощью {@link BasicMetrics#getPercentile(int)}
     *     </li>
     * </ul>
     * <hr>
     * Если данных нет, возвращаются значения по умолчанию
     */
    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        BasicMetrics metric = statistics.getMetric(BasicMetrics.class);
        return List.of(
            List.of("Файл", formatLink(statistics.source())),
            List.of("Количество запросов", formatNum(metric.totalRequests())),
            List.of("Средний размер ответа", formatNum(metric.averageResponseSize()) + " b"),
            List.of("95p размера ответа", formatNum(metric.getPercentile(PERCENTILE_95)) + " b"),
            List.of("50p размера ответа", formatNum(metric.getPercentile(PERCENTILE_50)) + " b")
        );
    }
}
