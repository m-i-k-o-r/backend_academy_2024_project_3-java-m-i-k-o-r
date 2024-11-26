package backend.academy.manager;

import backend.academy.filter.LogFilter;
import backend.academy.source.reader.DataReader;
import backend.academy.statistics.LogStatistics;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AllArgsConstructor
public class StatisticsManager {
    private final LogFilter filters;

    private static final Logger LOGGER = LogManager.getLogger(StatisticsManager.class);

    public List<LogStatistics> collectStatistics(List<DataReader> dataReaders) {
        return dataReaders.stream()
            .peek(dataReader -> LOGGER.info("Начало чтения данных из источника: '{}'", dataReader.getSource()))
            .map(dataReader -> {
                try {
                    return dataReader.read(filters);
                } catch (RuntimeException e) {
                    LOGGER.error("Ошибка при чтении данных из источника: {}. Причина: {}",
                        dataReader.getSource(), e.getMessage());

                    return null;
                }
            })
            .filter(Objects::nonNull)
            .peek(stats -> LOGGER.info("Завершено чтение данных из источника: '{}'", stats.source()))
            .toList();
    }
}
