package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

public class LogFilter {
    private final Map<FilterField, Predicate<LogRecord>> filters = new EnumMap<>(FilterField.class);

    public void addFilter(String field, String regex) {
        FilterField logField = FilterField.valueOf(field.toUpperCase());
        Predicate<LogRecord> predicate = createPredicate(logField, regex);
        filters.put(logField, predicate);
    }

    public boolean matches(LogRecord record) {
        return filters.entrySet().stream()
            .allMatch(entry -> entry.getValue().test(record));
    }

    private Predicate<LogRecord> createPredicate(FilterField field, String value) {
        switch (field) {
            case FROM, TO: {
                LocalDateTime dateTime = LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return logRecord -> field == FilterField.FROM
                    ? logRecord.timeLocal().isAfter(dateTime)
                    : logRecord.timeLocal().isBefore(dateTime);
            }

            default:
                throw new IllegalArgumentException("Неизвестный фильтр: " + field);
        }
    }
}

