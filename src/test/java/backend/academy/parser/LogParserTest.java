package backend.academy.parser;

import backend.academy.model.LogRecord;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogParserTest {
    @Test
    void testParseLogLineValidLog() {
        String logLine = "1.0.0.1 - - [29/Nov/2024:15:10:00 +0000] "
            + "\"GET /downloads/product_1 HTTP/1.1\" 200 2578 "
            + "\"https://test.com\" \"Debian APT-HTTP/1.3 (0.9.7.9)\"";

        LogRecord record = LogParser.parseLogLine(logLine);

        assertEquals("1.0.0.1", record.remoteAddr());
        assertEquals("-", record.remoteUser());
        assertEquals(LocalDateTime.of(2024, 11, 29, 15, 10), record.timeLocal());
        assertEquals("GET", record.request().method());
        assertEquals("/downloads/product_1", record.request().path());
        assertEquals("HTTP/1.1", record.request().protocol());
        assertEquals(200, record.status());
        assertEquals(2578, record.bodyBytesSent());
        assertEquals("https://test.com", record.httpReferer());
        assertEquals("Debian APT-HTTP/1.3 (0.9.7.9)", record.httpUserAgent());
    }

    @Test
    void testParseLogLineInvalidLog() {
        String invalidLogLine = "beleberda";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> LogParser.parseLogLine(invalidLogLine));

        assertTrue(exception.getMessage().contains("Не удалось сопоставить строку с паттерном"));
    }

    @Test
    void testParseLogLineInvalidDate() {
        String invalidLogLine = "1.0.0.1 - - [not-a-date] "
            + "\"GET /downloads/product_1 HTTP/1.1\" 200 2578 "
            + "\"https://test.com\" \"Debian APT-HTTP/1.3 (0.9.7.9)\"";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> LogParser.parseLogLine(invalidLogLine));

        assertTrue(exception.getMessage().contains("Ошибка при парсинге даты"));
    }

    @Test
    void testParseLogLineInvalidRequest() {
        String invalidLogLine = "1.0.0.1 - - [29/Nov/2024:15:10:00 +0000] "
            + "\"NOT-A-REQUEST\" 200 2578 "
            + "\"https://test.com\" \"Debian APT-HTTP/1.3 (0.9.7.9)\"";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> LogParser.parseLogLine(invalidLogLine));

        assertTrue(exception.getMessage().contains("Ошибка при парсинге запроса"));
    }

    @Test
    void testParseLogLineEmptyLogLine() {
        String invalidLogLine = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> LogParser.parseLogLine(invalidLogLine));

        assertTrue(exception.getMessage().contains("Не удалось сопоставить строку с паттерном"));
    }
}
