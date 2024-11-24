package backend.academy.report.sections;

import backend.academy.report.ReportGenerator;
import backend.academy.statistics.LogStatistics;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public abstract class Section {

    public void write(BufferedWriter writer, LogStatistics statistics, ReportGenerator generator) throws IOException {

        generator.writeHeader(writer, getHeader());

        for (List<String> row : prepareInfo(statistics)) {
            generator.writeInfo(writer, listToArray(row));
        }

        generator.writeTableHeader(writer, listToArray(getTableHeaders()));

        for (List<String> row : prepareRows(statistics)) {
            generator.writeTableRow(writer, listToArray(row));
        }

        generator.writeTableEnd(writer);
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
