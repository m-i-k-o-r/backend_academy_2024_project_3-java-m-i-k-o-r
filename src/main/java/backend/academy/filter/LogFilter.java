package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;
import lombok.Getter;

@Getter
public class LogFilter {
    private final Map<FilterField, Predicate<LogRecord>> filters = new EnumMap<>(FilterField.class);

    private final Map<FilterField, String> filterValues = new EnumMap<>(FilterField.class);

    public boolean matches(LogRecord record) {
        return filters.entrySet().stream()
            .allMatch(entry -> entry.getValue().test(record));
    }

    public void addFilter(String field, String regex) {
        FilterField logField = parseFilterField(field);

        addFilterInternal(
            logField,
            regex,
            new StringFilterStrategy(logField)
        );
    }

    public void addFilter(String field, LocalDateTime date) {
        FilterField logField = parseFilterField(field);

        addFilterInternal(
            logField,
            date,
            new DateFilterStrategy(logField)
        );
    }

    private <T> void addFilterInternal(FilterField logField, T value, LogFilterStrategy<T> strategy) {
        Predicate<LogRecord> predicate = strategy.createPredicate(value);

        filters.put(logField, predicate);

        String stringValue = value instanceof LocalDateTime
            ? ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"))
            : value.toString();
        filterValues.put(logField, stringValue);
    }

    private FilterField parseFilterField(String field) {
        try {
            return FilterField.valueOf(field.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(" ! Неизвестный фильтр: " + field, e);
        }
    }
}
