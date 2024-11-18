package backend.academy.report;

import java.io.BufferedWriter;
import java.io.IOException;

public class MarkdownReportGenerator extends ReportGenerator {
    @Override
    public void writeHeader(BufferedWriter writer, String header) throws IOException {
        writer.write("## " + header + "\n\n");
    }

    @Override
    public void writeTableHeader(BufferedWriter writer, String... headers) throws IOException {
        writer.write("| " + String.join(" | ", headers) + " |\n");
        writer.write("|" + "---|".repeat(headers.length) + "\n");
    }

    @Override
    public void writeTableRow(BufferedWriter writer, String... cells) throws IOException {
        writer.write("| " + String.join(" | ", cells) + " |\n");
    }

    @Override
    public void writeTableEnd(BufferedWriter writer) throws IOException {
        writer.write("---\n");
    }
}
