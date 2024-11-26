package backend.academy.report;

import java.io.BufferedWriter;
import java.io.IOException;

public interface ReportWriter {
    void writeHeader(BufferedWriter writer, String header) throws IOException;

    void writeInfo(BufferedWriter writer, String... headers) throws IOException;

    void writeTableHeader(BufferedWriter writer, String... headers) throws IOException;

    void writeTableRow(BufferedWriter writer, String... cells) throws IOException;

    void writeTableEnd(BufferedWriter writer) throws IOException;
}
