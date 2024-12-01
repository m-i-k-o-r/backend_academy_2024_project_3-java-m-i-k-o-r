package backend.academy.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormatConverterTest {
    private CliParams.FormatConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CliParams.FormatConverter();
    }

    @Test
    void testConvertReturnsExpectedFormat() {
        assertEquals(Format.MARKDOWN, converter.convert("markdown"));
        assertEquals(Format.ADOC, converter.convert("adoc"));
    }

    @Test
    void testConvertReturnsDefaultForInvalidInput() {
        assertEquals(Format.MARKDOWN, converter.convert("invalid"));
    }
}
