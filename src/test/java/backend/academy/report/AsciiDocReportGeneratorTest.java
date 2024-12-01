package backend.academy.report;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AsciiDocReportGeneratorTest {
    private AsciiDocReportGenerator format;
    private StringWriter output;
    private BufferedWriter writer;

    @BeforeEach
    void setUp() {
        format = new AsciiDocReportGenerator();
        output = new StringWriter();
        writer = new BufferedWriter(output);
    }

    @Test
    void testWriteHeader() throws IOException {
        format.writeHeader(writer, "Test Header");
        writer.flush();

        assertEquals("### Test Header\n\n", output.toString());
    }

    @Test
    void testWriteTableHeader() throws IOException {
        format.writeTableHeader(writer, "column_1", "column_2");
        writer.flush();

        String expected = "[format=\"csv\", options=\"header\"]\n|===\ncolumn_1,column_2\n";
        assertEquals(expected, output.toString());
    }

    @Test
    void testWriteTableRow() throws IOException {
        format.writeTableRow(writer, "value_1", "value_2");
        writer.flush();

        assertEquals("\"value_1\",\"value_2\"\n", output.toString());
    }

    @Test
    void testWriteTableEnd() throws IOException {
        format.writeTableEnd(writer);
        writer.flush();

        assertEquals("|===\n\n", output.toString());
    }
}
