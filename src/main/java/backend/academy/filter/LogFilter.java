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

@Getter
public class LogFilter {
    private static final Logger LOGGER = LogManager.getLogger(LogFilter.class);

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
