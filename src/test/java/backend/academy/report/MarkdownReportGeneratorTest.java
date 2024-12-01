package backend.academy.report;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarkdownReportGeneratorTest {
    private MarkdownReportGenerator format;
    private StringWriter output;
    private BufferedWriter writer;

    @BeforeEach
    void setUp() {
        format = new MarkdownReportGenerator();
        output = new StringWriter();
        writer = new BufferedWriter(output);
    }

    @Test
    void testWriteHeader() throws IOException {
        format.writeHeader(writer, "Test Header");
        writer.flush();
        assertEquals("## Test Header\n\n", output.toString());
    }

    @Test
    void testWriteTableHeader() throws IOException {
        format.writeTableHeader(writer, "column_1", "column_2");
        writer.flush();
        String expected = " | column_1 | column_2 | \n | --- | --- | \n";
        assertEquals(expected, output.toString());
    }

    @Test
    void testWriteTableRow() throws IOException {
        format.writeTableRow(writer, "value_1", "value_2");
        writer.flush();
        assertEquals(" | value_1 | value_2 | \n", output.toString());
    }

    @Test
    void testWriteTableEnd() throws IOException {
        format.writeTableEnd(writer);
        writer.flush();
        assertEquals("---\n", output.toString());
    }
}
