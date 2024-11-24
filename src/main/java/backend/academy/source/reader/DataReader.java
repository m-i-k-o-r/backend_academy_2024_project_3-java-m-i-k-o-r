package backend.academy.source.reader;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogRecord;
import backend.academy.parser.LogParser;
import backend.academy.statistics.LogStatistics;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DataReader {
    private static final Logger logger = LogManager.getLogger(DataReader.class);

    protected abstract BufferedReader createReader() throws IOException;

    public abstract String getSource();

    public LogStatistics read(LogFilter filter) {
        LogStatistics statistics = new LogStatistics(filter, getSource());
        logger.info("Начало чтения данных из источника: {}", getSource());
        try (BufferedReader in = createReader()) {
            String line;
            while ((line = in.readLine()) != null) {
                Optional<LogRecord> optionalRecord = LogParser.parseLogLine(line);

                if (optionalRecord.isEmpty()) {
                    continue;
                }

                LogRecord record = optionalRecord.get();
                if (filter.matches(record)) {
                    statistics.update(record);
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка при чтении данных из источника: {}. Причина: {}", getSource(), e.getMessage());
            return null;
        }
        logger.info("Завершено чтение данных из источника: {}", getSource());
        return statistics;
    }
}
