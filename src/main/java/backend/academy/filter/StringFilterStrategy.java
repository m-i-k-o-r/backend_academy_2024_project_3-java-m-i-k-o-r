package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import lombok.AllArgsConstructor;

/**
 * Реализация стратегии фильтрации логов по строковым значениям
 * <br>
 * Эта стратегия поддерживает фильтрацию по полям, содержащим строковые данные, такие как:
 * <ul>
 *     <li> <b>IP-адреса</b> - {@link FilterField#ADDRESS}</li>
 *     <li> <b>HTTP методы</b> - {@link FilterField#METHOD}</li>
 *     <li> <b>HTTP статусы</b> - {@link FilterField#STATUS}</li>
 *     <li> <b>Клиентские устройства</b> - {@link FilterField#AGENT}</li>
 * </ul>
 */
@AllArgsConstructor
public class StringFilterStrategy implements LogFilterStrategy<String> {

    /** Тип фильтрации лога */
    private final FilterField field;

    @Override
    public Predicate<LogRecord> createPredicate(String value) {
        if (field == FilterField.FROM || field == FilterField.TO) {
            throw new IllegalArgumentException("Фильтр по значению не доступен для полей 'FROM' и 'TO'");
        }

        Pattern pattern;
        try {
            pattern = Pattern.compile(value);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Некорректное регулярное выражение: " + value, e);
        }

        return logRecord -> {
            try {
                String valueRecord = getFieldValue(logRecord);
                return pattern.matcher(valueRecord).find();
            } catch (IllegalArgumentException e) {
                return false;
            }
        };
    }

    /**
     * Извлекает значение соответствующего поля из записи лога
     *
     * @param logRecord запись лога
     * @return значение поля в виде строки
     * @throws IllegalArgumentException если тип не поддерживается
     */
    private String getFieldValue(LogRecord logRecord) {
        return switch (field) {
            case ADDRESS -> logRecord.remoteAddr();
            case METHOD -> logRecord.request().method();
            case STATUS -> String.valueOf(logRecord.status());
            case AGENT -> logRecord.httpUserAgent();
            default -> throw new IllegalArgumentException("Неизвестное поле для фильтрации: " + field);
        };
    }
}
