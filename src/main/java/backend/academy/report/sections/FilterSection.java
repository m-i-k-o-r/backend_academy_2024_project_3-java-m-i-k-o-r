package backend.academy.report.sections;

import backend.academy.filter.FilterField;
import backend.academy.filter.LogFilter;
import backend.academy.statistics.LogStatistics;
import java.util.List;

/**
 * Класс для отображения секции с примененными фильтрами
 * <hr>
 * Используется для генерации отчета, показывающего параметры фильтрации,
 * применённые к данным логов
 * <br>
 * Данные извлекаются из объекта {@link LogFilter}
 */
public class FilterSection extends Section {
    @Override
    protected String getHeader() {
        return "Фильтры";
    }

    /**
     * Возвращает заголовки столбцов таблицы секции
     * <hr>
     * Таблица содержит следующие столбцы:
     * <ul>
     *     <li><b>название</b> - название фильтра</li>
     *     <li><b>значение</b> - значение, установленное для фильтра</li>
     * </ul>
     */
    @Override
    protected List<String> getTableHeaders() {
        return List.of("Название", "Значение");
    }

    /**
     * Подготавливает строки таблицы с информацией о примененных фильтрах
     * <hr>
     * Информация формируется на основе данных предоставленных методом {@link LogStatistics#filters()}
     * <hr>
     * Если данных нет, возвращается строка-заглушка ("-", "-")
     */
    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        LogFilter logFilter = statistics.filters();

        if (logFilter.filterValues().isEmpty()) {
            return List.of(List.of("-", "-"));
        }

        return logFilter.filterValues().entrySet().stream()
            .map(entry -> {
                FilterField field = entry.getKey();
                String filterValue = entry.getValue();

                return List.of(field.label(), filterValue);
            })
            .toList();
    }
}
