package backend.academy.utils;

import org.junit.jupiter.api.Test;
import static backend.academy.utils.Formatter.calculatePercentage;
import static backend.academy.utils.Formatter.formatCode;
import static backend.academy.utils.Formatter.formatHeaders;
import static backend.academy.utils.Formatter.formatLink;
import static backend.academy.utils.Formatter.formatNum;
import static backend.academy.utils.Formatter.generateBar;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormatterTest {
    @Test
    void testFormatNum() {
        assertEquals("1,234.56", formatNum(1234.56));
        assertEquals("123,456,789", formatNum(123456789));
        assertEquals("1,000", formatNum(1000));
        assertEquals("0.99", formatNum(0.99));
        assertEquals("0", formatNum(0));
    }

    @Test
    void testFormatCode() {
        assertEquals("`test`", formatCode("test"));
        assertEquals("`super duper long test`", formatCode("super duper long test"));
    }

    @Test
    void testFormatLink() {
        assertEquals("`file name`", formatLink("https://test.com/some-path/file%20name"));
        assertEquals("`fileName`", formatLink("http://test.com/another-some-path/fileName"));
        assertEquals("`test.log`", formatLink("С:/path/somePath/test.log"));
    }

    @Test
    void testFormatHeaders() {
        assertEquals("**Title**: Header1, Header2, Header3%n%n".formatted(),
            formatHeaders("Title", "Header1", "Header2", "Header3"));
        assertEquals("**Title**: -%n%n".formatted(), formatHeaders("Title"));
    }

    @Test
    void testCalculatePercentage() {
        assertEquals(50, calculatePercentage(50, 100));
        assertEquals(0, calculatePercentage(0, 100));
        assertEquals(100, calculatePercentage(100, 100));
        assertEquals(0, calculatePercentage(10, 0));
    }

    @Test
    void testGenerateBar() {
        assertEquals("██████████░░░░░░░░░░", generateBar(50, 100));
        assertEquals("████████████████████", generateBar(100, 100));
        assertEquals("░░░░░░░░░░░░░░░░░░░░", generateBar(0, 100));
        assertEquals("", generateBar(50, 0));
    }
}
