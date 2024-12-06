package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static backend.academy.utils.Constants.EXCLUSION_DETAILS;

/**
 * Класс для фильтрации записей логов на основе заданных условий
 * <br>
 * За что отвечает:
 * <ul>
 *   <li>создание фильтров для различных полей логов, включая:
 *       <ul>
 *           <li> по дате - {@link FilterField#TO} и {@link FilterField#FROM}</li>
 *           <li> по IP адресу - {@link FilterField#ADDRESS}</li>
 *           <li> по HTTP методу - {@link FilterField#METHOD}</li>
 *           <li> по HTTP статусу - {@link FilterField#STATUS}</li>
 *           <li> по клиентскому устройству - {@link FilterField#AGENT}</li>
 *       </ul>
 *   </li>
 *   <li>проверка, соответствует ли лог заданным фильтрам и условиям</li>
 * </ul>
 * <br>
 * Фильтры представляют собой предикаты (позволяют проверять соответствие лога заданным условиям)
 */
@Getter
public class LogFilter {
    private static final Logger LOGGER = LogManager.getLogger(LogFilter.class);

    /** Карта фильтров по полям, представленных предикатами */
    private final Map<FilterField, Predicate<LogRecord>> filters = new EnumMap<>(FilterField.class);

    /** Карта значений фильтров для хранения их условий в формате строки */
    private final Map<FilterField, String> filterValues = new EnumMap<>(FilterField.class);

    /**
     * Проверяет, соответствует ли лог всем заданным фильтрам
     *
     * @param currentLog текущая запись лога
     * @return true, если запись удовлетворяет всем фильтрам, иначе false
     */
    public boolean matches(LogRecord currentLog) {
        return filters.entrySet().stream()
            .allMatch(entry -> entry.getValue().test(currentLog));
    }

    /**
     * Добавляет фильтр по строковому значению для указанного поля
     *
     * @param field имя поля для фильтрации
     * @param value значение фильтра
     */
    public void addFilter(String field, String value) {
        parseFilterField(field).ifPresent(
            logField ->
                addFilterInternal(
                    logField,
                    value,
                    new StringFilterStrategy(logField)
                )
        );
    }

    /**
     * Добавляет фильтр по значению даты и времени для указанного поля
     *
     * @param field имя поля для фильтрации
     * @param value значение фильтра (дата и время)
     */
    public void addFilter(String field, LocalDateTime value) {
        parseFilterField(field).ifPresent(
            logField ->
                addFilterInternal(
                    logField,
                    value,
                    new DateFilterStrategy(logField)
                )
        );
    }

    /**
     * Внутренний метод для добавления фильтра
     *
     * @param logField поле для фильтрации
     * @param value    значение фильтра
     * @param strategy {@link LogFilterStrategy} (стратегия фильтрации)
     * @param <T>      тип значения фильтра
     */
    private <T> void addFilterInternal(FilterField logField, T value, LogFilterStrategy<T> strategy) {
        try {
            Predicate<LogRecord> predicate = strategy.createPredicate(value);
            filters.merge(logField, predicate, Predicate::or);

            String stringValue = value instanceof LocalDateTime
                ? ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"))
                : value.toString();
            filterValues.merge(logField, stringValue, (oldValue, newValue) -> oldValue + ", " + newValue);

            LOGGER.info("Фильтр успешно добавлен: {} - {}", logField, value);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Ошибка при добавлении фильтра: {} - {}", logField, value);
            LOGGER.debug(EXCLUSION_DETAILS, e);
        }
    }

    /**
     * Преобразование строки в тип {@link FilterField}
     *
     * @param field имя поля
     * @return {@link Optional}, содержащий {@link FilterField} при нахождении соответствующего типа
     */
    private Optional<FilterField> parseFilterField(String field) {
        try {
            return Optional.of(
                FilterField.valueOf(field.trim().toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            LOGGER.error("Фильтр не добавлен. Неизвестный фильтр: {}. Ошибка: {}", field, e.getMessage());
            LOGGER.debug(EXCLUSION_DETAILS, e);
            return Optional.empty();
        }
    }
}
