package backend.academy;

import backend.academy.cli.CliParams;
import backend.academy.cli.CliParser;
import backend.academy.filter.LogFilter;
import backend.academy.manager.DataReaderManager;
import backend.academy.manager.FilterManager;
import backend.academy.manager.ReportManager;
import backend.academy.manager.StatisticsManager;
import backend.academy.source.reader.DataReader;
import backend.academy.statistics.LogStatistics;
import com.beust.jcommander.ParameterException;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {

    public static void main(String[] args) {
        CliParser cliParser = new CliParser();
        CliParams params;
        try {
            params = cliParser.parse(args);
        } catch (ParameterException e) {
            cliParser.printUsage();
            return;
        }

        LogFilter logFilter = FilterManager.configureFilters(params);

        List<DataReader> dataReaders = DataReaderManager.createDataReaders(params.paths());
        List<LogStatistics> statistics = new StatisticsManager(logFilter).collectStatistics(dataReaders);

        ReportManager.generateReports(statistics, params.format());
    }
}
