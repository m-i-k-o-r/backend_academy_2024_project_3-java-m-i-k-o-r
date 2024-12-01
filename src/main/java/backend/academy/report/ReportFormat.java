package backend.academy.report;

import backend.academy.report.sections.FilterSection;
import backend.academy.report.sections.GeneralInfoSection;
import backend.academy.report.sections.MostActiveDaySection;
import backend.academy.report.sections.RequestMethodsSection;
import backend.academy.report.sections.RequestedResourcesSection;
import backend.academy.report.sections.ResponseCodesSection;
import backend.academy.report.sections.Section;
import backend.academy.report.sections.UserActivitySection;
import backend.academy.statistics.LogStatistics;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReportFormat {
    private final ReportGenerator format;
    private final List<Section> sections;

    public ReportFormat(ReportGenerator format) {
        this.format = format;
        this.sections = List.of(
            new GeneralInfoSection(),
            new FilterSection(),
            new RequestedResourcesSection(),
            new ResponseCodesSection(),
            new RequestMethodsSection(),
            new UserActivitySection(),
            new MostActiveDaySection()
        );
    }

    public void generateReport(String filename, LogStatistics statistics) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(filename), StandardCharsets.UTF_8)) {
            for (Section section : sections) {
                section.write(writer, statistics, format);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
