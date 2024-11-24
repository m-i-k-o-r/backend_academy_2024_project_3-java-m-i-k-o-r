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
import java.io.IOException;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {

    public static void main(String[] args) throws IOException {
        CliParams params = new CliParser().parse(args);
        LogFilter logFilter = FilterManager.configureFilters(params);

        List<DataReader> dataReaders = DataReaderManager.createDataReaders(params.paths());
        List<LogStatistics> statistics = new StatisticsManager(logFilter).collectStatistics(dataReaders);

        ReportManager.generateReports(statistics, params.format());
    }
}
