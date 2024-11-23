package backend.academy.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CliParams {
    @Parameter(names = "--path", required = true, variableArity = true)
    private List<String> paths;

    @Parameter(names = "--from", converter = LocalDateTimeConverter.class)
    private LocalDateTime from;

    @Parameter(names = "--to", converter = LocalDateTimeConverter.class)
    private LocalDateTime to;

    @Parameter(names = "--format", validateWith = FormatValidator.class)
    private String format = "markdown";

    @Parameter(names = "--filter-field", variableArity = true)
    private List<String> filterFields;

    @Parameter(names = "--filter-value", variableArity = true)
    private List<String> filterValues;

    public static class FormatValidator implements IParameterValidator {
        private static final List<String> ALLOWED_FORMATS = List.of("markdown", "adoc");

        @Override
        public void validate(String name, String value) throws IllegalArgumentException {
            if (!ALLOWED_FORMATS.contains(value)) {
                throw new IllegalArgumentException(
                    " ! Неверный формат вывода результата. Доступные форматы: " + ALLOWED_FORMATS);
            }
        }
    }

    public static class LocalDateTimeConverter implements IStringConverter<LocalDateTime> {

        @Override
        public LocalDateTime convert(String value) {
            try {
                return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException e) {
                try {
                    LocalDate date = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
                    return date.atStartOfDay();
                } catch (DateTimeParseException ex) {
                    throw new IllegalArgumentException(" ! Некорректный формат даты: " + value, ex);
                }
            }
        }
    }
}
