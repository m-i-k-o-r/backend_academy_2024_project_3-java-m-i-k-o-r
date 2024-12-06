package backend.academy.manager;

import backend.academy.cli.CliParams;
import backend.academy.filter.FilterField;
import backend.academy.filter.LogFilter;
import java.time.LocalDateTime;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * Утилитарный класс, отвечающий за настройку и
 * создание фильтров {@link LogFilter} на основе параметров CLI
 */
@UtilityClass
public class FilterManager {

    /**
     * Метод получает список полей и значений фильтров из параметров CLI и добавляет их в {@link LogFilter}
     * <br>
     * Также добавляются фильтры для дат (если они указаны)
     *
     * @param params параметры командной строки (CLI).
     * @return настроенный объект {@link LogFilter}.
     */
    public static LogFilter configureFilters(CliParams params) {
        LogFilter filters = new LogFilter();

        List<String> fields = params.filterFields();
        List<String> values = params.filterValues();

        if (fields != null && values != null) {
            for (int i = 0; i < fields.size(); i++) {
                filters.addFilter(fields.get(i), values.get(i));
            }
        }

        addDateFilter(filters, FilterField.TO, params.to());
        addDateFilter(filters, FilterField.FROM, params.from());

        return filters;
    }

    /**
     * Добавляет фильтр для даты в {@link LogFilter}.
     * <br>
     * Если дата указана, она добавляется в фильтр для соответствующего типа {@link FilterField}
     *
     * @param filters {@link LogFilter}, в который добавляется фильтр
     * @param field   поле фильтра
     * @param date    дата для фильтра
     */
    private static void addDateFilter(LogFilter filters, FilterField field, LocalDateTime date) {
        if (date != null) {
            filters.addFilter(String.valueOf(field), date);
        }
    }
}
