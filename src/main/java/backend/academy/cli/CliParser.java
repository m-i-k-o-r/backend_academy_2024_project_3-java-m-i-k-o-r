package backend.academy.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import lombok.Getter;

public class CliParser {
    @Getter
    private final CliParams cliParams;
    private final JCommander jCommander;

    public CliParser() {
        this.cliParams = new CliParams();
        this.jCommander = JCommander.newBuilder()
            .addObject(cliParams)
            .build();
    }

    public void parseArgs(String[] args) {
        try {
            jCommander.parse(args);
            validateFilters();
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(1);
        }
    }

    private void validateFilters() {
        if (cliParams.filterFields() == null && cliParams.filterValues() == null) {
            return;
        }

        if ((cliParams.filterFields() != null && cliParams.filterValues() == null) ||
            (cliParams.filterFields() == null && cliParams.filterValues() != null)) {
            throw new ParameterException(
                "Размер массива полей и массива значений для фильтрации не совпадают"
            );
        }

        if (cliParams.filterFields().size() != cliParams.filterValues().size()) {
            throw new ParameterException(
                "Размер массива полей и массива значений для фильтрации не совпадают. "
                    + "Поля: " + cliParams.filterFields() + ", Значения: " + cliParams.filterValues()
            );
        }
    }

    public void printUsage() {
        jCommander.usage();
    }
}
