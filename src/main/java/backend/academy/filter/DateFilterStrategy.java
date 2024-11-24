package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DateFilterStrategy implements LogFilterStrategy<LocalDateTime> {
    private final FilterField field;

    @Override
    public Predicate<LogRecord> createPredicate(LocalDateTime dateTime) {
        if (field != FilterField.FROM && field != FilterField.TO) {
            throw new IllegalArgumentException("Фильтр по дате доступен только для полей 'FROM' и 'TO'");
        }

        return logRecord ->
            field == FilterField.FROM
                ? logRecord.timeLocal().isAfter(dateTime)
                : logRecord.timeLocal().isBefore(dateTime);
    }
}
