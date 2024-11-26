package backend.academy.report.sections;

import backend.academy.report.ReportWriter;
import backend.academy.statistics.LogStatistics;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public abstract class Section {

    public void write(BufferedWriter writer, LogStatistics statistics, ReportWriter reportWriter) throws IOException {

        reportWriter.writeHeader(writer, getHeader());

        for (List<String> row : prepareInfo(statistics)) {
            reportWriter.writeInfo(writer, listToArray(row));
        }

        reportWriter.writeTableHeader(writer, listToArray(getTableHeaders()));

        for (List<String> row : prepareRows(statistics)) {
            reportWriter.writeTableRow(writer, listToArray(row));
        }

        reportWriter.writeTableEnd(writer);
    }

    protected abstract String getHeader();

    protected abstract List<String> getTableHeaders();

    protected abstract List<List<String>> prepareRows(LogStatistics statistics);

    protected List<List<String>> prepareInfo(LogStatistics statistics) {
        return List.of();
    }

    private String[] listToArray(List<String> list) {
        return list.toArray(new String[0]);
    }
}
