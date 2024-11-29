package backend.academy.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AsciiDocReportGenerator extends ReportGenerator {
    @Override
    public void writeHeader(BufferedWriter writer, String header) throws IOException {
        writer.write("### " + header + "\n\n");
    }

    @Override
    public void writeTableHeader(BufferedWriter writer, String... headers) throws IOException {
        writer.write("[format=\"csv\", options=\"header\"]\n");
        writer.write("|===\n");
        writer.write(String.join(",", headers) + "\n");
    }

    @Override
    public void writeTableRow(BufferedWriter writer, String... cells) throws IOException {
        String row = Arrays.stream(cells)
            .map(cell -> "\"" + cell + "\"")
            .collect(Collectors.joining(","));

        writer.write(row + "\n");
    }

    @Override
    public void writeTableEnd(BufferedWriter writer) throws IOException {
        writer.write("|===\n\n");
    }
}
