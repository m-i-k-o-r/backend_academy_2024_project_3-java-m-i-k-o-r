package backend.academy.file;

import backend.academy.statistics.LogRecord;
import backend.academy.statistics.LogStatistics;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public abstract class DataReader {

    protected abstract BufferedReader getReader() throws IOException;

    public LogStatistics read() {
        LogStatistics statistics = new LogStatistics();
        try (BufferedReader in = getReader()) {
            String line;
            while ((line = in.readLine()) != null) {
                Optional<LogRecord> optionalRecord = LogRecord.fromLogLine(line);

                if (optionalRecord.isEmpty()) continue;

                LogRecord record = optionalRecord.get();
                statistics.update(record);
            }
        } catch (IOException e) {
            System.err.println(" ! Ошибка при чтении данных: " + e.getMessage());
        }
        return statistics;
    }
}
