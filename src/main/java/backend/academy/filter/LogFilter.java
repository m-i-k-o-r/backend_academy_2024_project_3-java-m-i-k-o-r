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

@Getter
public class LogFilter {
    private static final Logger logger = LogManager.getLogger(LogFilter.class);

    private final Map<FilterField, Predicate<LogRecord>> filters = new EnumMap<>(FilterField.class);

    private final Map<FilterField, String> filterValues = new EnumMap<>(FilterField.class);

    public boolean matches(LogRecord currentLog) {
        return filters.entrySet().stream()
            .allMatch(entry -> entry.getValue().test(currentLog));
    }

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

    private <T> void addFilterInternal(FilterField logField, T value, LogFilterStrategy<T> strategy) {
        try {
            Predicate<LogRecord> predicate = strategy.createPredicate(value);
            filters.put(logField, predicate);

            String stringValue = value instanceof LocalDateTime
                ? ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"))
                : value.toString();
            filterValues.put(logField, stringValue);

            logger.info("Фильтр успешно добавлен: {} - {}", logField, value);
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при добавлении фильтра: {} - {}", logField, value);
            logger.debug("Детали исключения:", e);
        }
    }

    private Optional<FilterField> parseFilterField(String field) {
        try {
            return Optional.of(
                FilterField.valueOf(field.trim().toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            logger.error("Фильтр не добавлен. Неизвестный фильтр: {}. Ошибка: {}", field, e.getMessage());
            logger.debug("Детали исключения:", e);
            return Optional.empty();
        }
    }
}
