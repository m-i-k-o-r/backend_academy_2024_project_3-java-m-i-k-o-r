package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StringFilterStrategy implements LogFilterStrategy<String> {
    private final FilterField field;

    @Override
    public Predicate<LogRecord> createPredicate(String value) {
        Pattern pattern;
        try {
            pattern = Pattern.compile(value);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException(" ! Некорректное регулярное выражение: " + value, e);
        }
        return logRecord -> pattern.matcher(getFieldValue(logRecord)).matches();
    }

    private String getFieldValue(LogRecord logRecord) {
        return switch (field) {
            case ADDRESS -> logRecord.remoteAddr();
            case METHOD -> logRecord.request().method();
            case STATUS -> String.valueOf(logRecord.status());
            case AGENT -> logRecord.httpUserAgent();
            default -> throw new IllegalArgumentException(" ! Неизвестное поле для фильтрации: " + field);
        };
    }
}
