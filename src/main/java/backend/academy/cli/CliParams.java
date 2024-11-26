package backend.academy.cli;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
@ToString
public class CliParams {
    private static final Logger LOGGER = LogManager.getLogger(CliParams.class);

    @Parameter(names = "--path",
        required = true,
        variableArity = true,
        description = "Путь к log-файлам или URL")
    private List<String> paths;

    @Parameter(names = "--from",
        converter = LocalDateTimeConverter.class,
        description = "Начальная дата/время (в формате ISO 8601)")
    private LocalDateTime from;

    @Parameter(names = "--to",
        converter = LocalDateTimeConverter.class,
        description = "Конечная дата/время (в формате ISO 8601)")
    private LocalDateTime to;

    @Parameter(names = "--format",
        converter = FormatConverter.class,
        description = "Формат вывода результата")
    private Format format = Format.MARKDOWN;

    @Setter
    @Parameter(names = "--filter-field",
        variableArity = true,
        description = "Список полей для фильтрации")
    private List<String> filterFields;

    @Setter
    @Parameter(names = "--filter-value",
        variableArity = true,
        description = "Список значений для фильтрации")
    private List<String> filterValues;

    public static class FormatConverter implements IStringConverter<Format> {

        @Override
        public Format convert(String value) {
            try {
                return Format.valueOf(value.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                LOGGER.error("Неверный формат: {}. Доступные форматы: {}", value, Format.getAvailableFormats());
                LOGGER.warn("Выбран формат вывода по умолчанию: {}", Format.MARKDOWN);

                return Format.MARKDOWN;
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
                    LOGGER.error("Некорректный формат даты: {}. Ожидается формат ISO 8601", value);
                    return null;
                }
            }
        }
    }
}
