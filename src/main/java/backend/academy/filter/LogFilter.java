package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;
import lombok.Getter;

@Getter
public class LogFilter {
    private final Map<FilterField, Predicate<LogRecord>> filters = new EnumMap<>(FilterField.class);

    private final Map<FilterField, String> filterValues = new EnumMap<>(FilterField.class);

    public void addFilter(String field, String regex) {
        FilterField logField = FilterField.valueOf(field.toUpperCase());
        Predicate<LogRecord> predicate = createPredicate(logField, regex);
        filters.put(logField, predicate);
        filterValues.put(logField, regex);
    }

    public boolean matches(LogRecord record) {
        return filters.entrySet().stream()
            .allMatch(entry -> entry.getValue().test(record));
    }

    private Predicate<LogRecord> createPredicate(FilterField field, String value) {
        switch (field) {
            case FROM, TO: {
                LocalDateTime dateTime = LocalDateTime.parse(value);
                return logRecord -> field == FilterField.FROM
                    ? logRecord.timeLocal().isAfter(dateTime)
                    : logRecord.timeLocal().isBefore(dateTime);
            }

            default:
                throw new IllegalArgumentException("Неизвестный фильтр: " + field);
        }
    }
}

