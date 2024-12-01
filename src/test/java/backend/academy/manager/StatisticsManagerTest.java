package backend.academy.manager;

import backend.academy.filter.LogFilter;
import backend.academy.source.reader.DataReader;
import backend.academy.statistics.LogStatistics;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StatisticsManagerTest {
    LogFilter filter = mock(LogFilter.class);

    @Test
    void testCollectStatisticsWithValidReaders() {
        DataReader reader1 = mock(DataReader.class);
        DataReader reader2 = mock(DataReader.class);

        LogStatistics stats1 = new LogStatistics(filter, "source1");
        LogStatistics stats2 = new LogStatistics(filter, "source2");

        when(reader1.read(filter)).thenReturn(stats1);
        when(reader2.read(filter)).thenReturn(stats2);

        StatisticsManager manager = new StatisticsManager(filter);
        List<LogStatistics> statistics = manager.collectStatistics(List.of(reader1, reader2));

        assertEquals(2, statistics.size());
        assertEquals(stats1, statistics.get(0));
        assertEquals(stats2, statistics.get(1));

        verify(reader1).read(filter);
        verify(reader2).read(filter);
    }

    @Test
    void testCollectStatisticsWithExceptionInReader() {
        DataReader validReader = mock(DataReader.class);
        DataReader failingReader = mock(DataReader.class);

        LogStatistics validStats = new LogStatistics(filter, "source");

        when(validReader.read(filter)).thenReturn(validStats);
        when(failingReader.read(filter)).thenThrow(new RuntimeException());

        StatisticsManager manager = new StatisticsManager(filter);
        List<LogStatistics> statistics = manager.collectStatistics(List.of(validReader, failingReader));

        assertEquals(1, statistics.size());
        assertEquals(validStats, statistics.getFirst());

        verify(validReader).read(filter);
        verify(failingReader).read(filter);
    }

    @Test
    void testCollectStatisticsWithEmptyReaderList() {
        StatisticsManager manager = new StatisticsManager(filter);
        List<LogStatistics> statistics = manager.collectStatistics(List.of());

        assertTrue(statistics.isEmpty());
    }

    @Test
    void testCollectStatisticsWithNullReader() {
        DataReader nullReader = mock(DataReader.class);
        when(nullReader.read(filter)).thenReturn(null);

        StatisticsManager manager = new StatisticsManager(filter);
        List<LogStatistics> statistics = manager.collectStatistics(List.of(nullReader));

        assertTrue(statistics.isEmpty());
        verify(nullReader).read(filter);
    }
}
