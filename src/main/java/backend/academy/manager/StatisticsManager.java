package backend.academy.manager;

import backend.academy.filter.LogFilter;
import backend.academy.source.reader.DataReader;
import backend.academy.statistics.LogStatistics;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StatisticsManager {
    private final LogFilter filters;

    public List<LogStatistics> collectStatistics(List<DataReader> dataReaders) {
        return dataReaders.stream()
            .map(dataReader -> dataReader.read(filters))
            .filter(Objects::nonNull)
            .toList();
    }
}
