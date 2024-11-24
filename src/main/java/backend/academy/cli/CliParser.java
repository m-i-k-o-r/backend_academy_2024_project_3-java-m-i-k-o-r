package backend.academy.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.util.Collections;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CliParser {
    @Getter
    private final CliParams cliParams;
    private final JCommander jCommander;

    private static final Logger logger = LogManager.getLogger(CliParser.class);

    public CliParser() {
        this.cliParams = new CliParams();
        this.jCommander = JCommander.newBuilder()
            .addObject(cliParams)
            .build();
    }

    public CliParams parse(String[] args) {
        try {
            jCommander.parse(args);
            validateFilters();
        } catch (ParameterException e) {
            logger.warn("Ошибка при парсинге: {}", e.getMessage());
            logger.debug(e);

            printUsage();
            System.exit(1);
        }
        return cliParams;
    }

    private void validateFilters() {
        if (cliParams.filterFields() == null && cliParams.filterValues() == null) {
            return;
        }

        if ((cliParams.filterFields() == null || cliParams.filterValues() == null)
            || (cliParams.filterFields().size() != cliParams.filterValues().size())) {

            logger.error("Все фильтры были сброшены и заменены на пустые");
            logger.warn("Размеры массивов filterFields и filterValues не совпадают. "
                + "filterFields: {}, filterValues: {}", cliParams.filterFields(), cliParams.filterValues());

            cliParams.filterFields(Collections.emptyList());
            cliParams.filterValues(Collections.emptyList());
        }
    }

    public void printUsage() {
        jCommander.usage();
    }
}
