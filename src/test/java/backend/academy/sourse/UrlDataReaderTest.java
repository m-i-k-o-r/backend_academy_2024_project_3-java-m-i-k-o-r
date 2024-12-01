package backend.academy.sourse;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogRecord;
import backend.academy.source.reader.UrlDataReader;
import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.BasicMetrics;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UrlDataReaderTest {
    private HttpServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/", exchange -> {
            List<String> logs = List.of(
                "195.1.24.132 - - [04/Jun/2015:07:06:24 +0000] \"GET /downloads/product_2 HTTP/1.1\" 200 2578 \"-\" \"urlgrabber/3.1.0 yum/3.2.22\"",
                "80.91.33.133 - - [04/Jun/2015:07:06:05 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.22)\""
            );

            String response = String.join("\n", logs);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void testReadFromUrl() throws MalformedURLException {
        LogFilter filter = Mockito.spy(new LogFilter());
        URL url = URI.create("http://localhost:" + server.getAddress().getPort() + "/").toURL();
        UrlDataReader reader = new UrlDataReader(url);

        LogStatistics stats = reader.read(filter);

        assertEquals(url.toString(), stats.source());
        assertEquals(2, stats.getMetric(BasicMetrics.class).totalRequests());
        Mockito.verify(filter, Mockito.times(2)).matches(Mockito.any(LogRecord.class));
    }

    @Test
    void testGetSource() throws MalformedURLException {
        URL url = URI.create("http://localhost:" + server.getAddress().getPort() + "/").toURL();
        UrlDataReader reader = new UrlDataReader(url);
        assertEquals(url.toString(), reader.getSource());
    }

    @Test
    void testInvalidUrl() {
        assertThrows(RuntimeException.class, () -> {
            URL url = URI.create("http://invalid.url").toURL();
            UrlDataReader reader = new UrlDataReader(url);
            reader.read(new LogFilter());
        });
    }
}
