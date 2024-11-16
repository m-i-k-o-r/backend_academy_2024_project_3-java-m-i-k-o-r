package backend.academy.source.file;

import backend.academy.filter.LogFilter;
import backend.academy.parser.LogParser;
import backend.academy.model.LogRecord;
import backend.academy.statistics.LogStatistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public abstract class DataReader {

    public abstract BufferedReader createReader() throws IOException;

    public abstract String getSource();

    public LogStatistics read(LogFilter filter) {
        LogStatistics statistics = new LogStatistics(filter, getSource());
        try (BufferedReader in = createReader()) {
            String line;
            while ((line = in.readLine()) != null) {
                Optional<LogRecord> optionalRecord = LogParser.parseLogLine(line);

                if (optionalRecord.isEmpty()) continue;

                LogRecord record = optionalRecord.get();
                if (filter.matches(record)) {
                    statistics.update(record);
                }
            }
        } catch (IOException e) {
            System.err.println(" ! Ошибка при чтении данных: " + e.getMessage());
        }
        return statistics;
    }
}

