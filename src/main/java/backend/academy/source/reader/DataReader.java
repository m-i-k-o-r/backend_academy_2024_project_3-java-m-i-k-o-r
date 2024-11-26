package backend.academy.source.reader;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogRecord;
import backend.academy.parser.LogParser;
import backend.academy.statistics.LogStatistics;
import java.io.BufferedReader;
import java.io.IOException;

public abstract class DataReader {
    protected abstract BufferedReader createReader() throws IOException;

    public abstract String getSource();

    public LogStatistics read(LogFilter filter) throws RuntimeException {
        LogStatistics statistics = new LogStatistics(filter, getSource());
        try (BufferedReader in = createReader()) {
            String line;
            while ((line = in.readLine()) != null) {
                LogRecord entry = LogParser.parseLogLine(line);

                if (filter.matches(entry)) {
                    statistics.update(entry);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении данных из источника: " + getSource(), e);
        }
        return statistics;
    }
}
