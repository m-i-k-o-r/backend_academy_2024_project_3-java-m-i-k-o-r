package backend.academy.report;

import java.io.BufferedWriter;
import java.io.IOException;
import static backend.academy.utils.Formatter.formatHeaders;

public class MarkdownReportGenerator implements ReportGenerator {
    private static final String HEADER_PREFIX = "## ";
    private static final String NEWLINE = "\n";
    private static final String ROW_SEPARATOR = " | ";
    private static final String LINE_SEPARATOR = "---";

    @Override
    public void writeHeader(BufferedWriter writer, String header) throws IOException {
        writer.write(HEADER_PREFIX + header + NEWLINE.repeat(2));
    }

    @Override
    public void writeInfo(BufferedWriter writer, String... cells) throws IOException {
        writer.write(formatHeaders(cells));
    }

    @Override
    public void writeTableHeader(BufferedWriter writer, String... headers) throws IOException {
        writer.write(formatRow(headers) + NEWLINE);
        writer.write(formatSeparator(headers.length) + NEWLINE);
    }

    @Override
    public void writeTableRow(BufferedWriter writer, String... cells) throws IOException {
        writer.write(formatRow(cells) + NEWLINE);
    }

    @Override
    public void writeTableEnd(BufferedWriter writer) throws IOException {
        writer.write(LINE_SEPARATOR + NEWLINE);
    }

    private String formatRow(String... cells) {
        return ROW_SEPARATOR + String.join(ROW_SEPARATOR, cells) + ROW_SEPARATOR;
    }

    private String formatSeparator(int columnCount) {
        return ROW_SEPARATOR + (LINE_SEPARATOR + ROW_SEPARATOR).repeat(columnCount);
    }
}
