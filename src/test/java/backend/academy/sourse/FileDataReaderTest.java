package backend.academy.sourse;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogRecord;
import backend.academy.source.reader.FileDataReader;
import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.BasicMetrics;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileDataReaderTest {
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("test-log", ".log");
        Files.write(tempFile, List.of(
            "195.1.24.132 - - [04/Jun/2015:07:06:24 +0000] \"GET /downloads/product_2 HTTP/1.1\" 200 2578 \"-\" \"urlgrabber/3.1.0 yum/3.2.22\"",
            "\"80.91.33.133 - - [04/Jun/2015:07:06:05 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.22)\""
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testReadLogFile() {
        LogFilter filter = Mockito.spy(new LogFilter());
        FileDataReader reader = new FileDataReader(tempFile);

        LogStatistics stats = reader.read(filter);

        assertEquals(tempFile.toUri().toString(), stats.source());
        assertEquals(2, stats.getMetric(BasicMetrics.class).totalRequests());
        Mockito.verify(filter, Mockito.times(2)).matches(Mockito.any(LogRecord.class));
    }

    @Test
    void testGetSource() {
        FileDataReader reader = new FileDataReader(tempFile);
        assertEquals(tempFile.toUri().toString(), reader.getSource());
    }
}
