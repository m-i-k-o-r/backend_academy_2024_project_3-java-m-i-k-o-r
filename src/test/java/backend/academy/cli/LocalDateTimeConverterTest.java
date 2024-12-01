package backend.academy.cli;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LocalDateTimeConverterTest {
    private CliParams.LocalDateTimeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CliParams.LocalDateTimeConverter();
    }

    @Test
    void testConvertDateTime() {
        LocalDateTime expected = LocalDateTime.of(2024, 11, 29, 15, 10);
        assertEquals(expected, converter.convert("2024-11-29T15:10:00"));
    }

    @Test
    void testConvertDate() {
        LocalDateTime expected = LocalDate.of(2024, 11, 29).atStartOfDay();
        assertEquals(expected, converter.convert("2024-11-29"));
    }

    @Test
    void testReturnNullForInvalidInput() {
        assertNull(converter.convert("invalid"));
    }
}
