package backend.academy.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CliParams {
    @Parameter(names = "--path", required = true, variableArity = true)
    private List<String> paths;

    @Parameter(names = "--from")
    private String from;

    @Parameter(names = "--to")
    private String to;

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
                throw new IllegalArgumentException("Неверный формат. Форматы доступные для выбора: " + ALLOWED_FORMATS);
            }
        }
    }
}
