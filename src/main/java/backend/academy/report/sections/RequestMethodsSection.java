package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.RequestMetrics;
import java.util.List;
import static backend.academy.utils.Formatter.formatNum;

/**
 * Класс для отображения секции с информацией о частоте использования HTTP методов
 * <hr>
 * Используется для генерации отчета, содержащего таблицу с методами HTTP запросов
 * и количеством их использования
 * <br>
 * Данные извлекаются из метрики {@link RequestMetrics}
 */
public class RequestMethodsSection extends Section {
    @Override
    protected String getHeader() {
        return "Методы запросов";
    }

    /**
     * Возвращает заголовки столбцов таблицы секции
     * <hr>
     * Таблица содержит следующие столбцы:
     * <ul>
     *     <li><b>метод</b> - название HTTP метода</li>
     *     <li><b>кол-во</b> - число запросов с данным методом</li>
     * </ul>
     */
    @Override
    protected List<String> getTableHeaders() {
        return List.of("Метод", "Кол-во");
    }

    /**
     * Подготавливает строки таблицы с данными о частоте использования HTTP-методов
     * <hr>
     * Информация формируется на основе частотного распределения методов запросов,
     * полученных из {@link RequestMetrics#getMethodFrequency()}
     * <hr>
     * Если данных нет, возвращается строка-заглушка ("-", "-")
     */
    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        RequestMetrics metrics = statistics.getMetric(RequestMetrics.class);

        List<List<String>> methodFrequency = metrics.getMethodFrequency().entrySet().stream()
            .map(entry -> List.of(entry.getKey(), formatNum(entry.getValue())))
            .toList();

        if (methodFrequency.isEmpty()) {
            return List.of(List.of("-", "-"));
        }

        return methodFrequency;
    }
}
