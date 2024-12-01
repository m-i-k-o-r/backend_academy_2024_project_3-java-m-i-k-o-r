package backend.academy.cli;

import com.beust.jcommander.ParameterException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CliParserTest {
    private CliParser parser;

    @BeforeEach
    void setUp() {
        parser = new CliParser();
    }

    @Test
    void testParseValidArguments() {
        String[] args = {
            "--path", "log.txt",
            "--from", "2024-11-01T00:00:00",
            "--to", "2024-11-29T15:10:00"
        };

        CliParams params = parser.parse(args);

        assertNotNull(params);
        assertEquals(List.of("log.txt"), params.paths());
        assertEquals("2024-11-01T00:00", params.from().toString());
        assertEquals("2024-11-29T15:10", params.to().toString());
    }

    @Test
    void testParseInvalidFiltersSize() {
        String[] args = {
            "--path", "/logs",
            "--filter-field", "field1", "field2",
            "--filter-value", "value1"
        };

        CliParams params = parser.parse(args);

        assertTrue(params.filterFields().isEmpty());
        assertTrue(params.filterValues().isEmpty());
    }

    @Test
    void testMissingRequiredArgs() {
        String[] args = {};
        assertThrows(ParameterException.class, () -> parser.parse(args));
    }
}
