package backend.academy.manager;

import backend.academy.cli.Format;
import backend.academy.filter.LogFilter;
import backend.academy.report.ReportFormat;
import backend.academy.statistics.LogStatistics;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

public class ReportManagerTest {
    @Test
    void testGenerateReportsWithMarkdownFormat() throws IOException {
        LogStatistics statistic = new LogStatistics(new LogFilter(), "source");

        LocalDateTime fixedTime = LocalDateTime.of(2024, 11, 30, 15, 10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM_HH-mm-ss-SSS");

        try (var mockedStaticTime = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS)) {
            mockedStaticTime.when(LocalDateTime::now).thenReturn(fixedTime);

            try (var mockedMarkdown = mockConstruction(ReportFormat.class)) {
                ReportManager.generateReports(List.of(statistic), Format.MARKDOWN);

                String expectedFileName = "report_" + fixedTime.format(formatter) + Format.MARKDOWN.label();

                List<ReportFormat> createdMocks = mockedMarkdown.constructed();
                assertEquals(1, createdMocks.size());

                ReportFormat mockGenerator = createdMocks.getFirst();
                verify(mockGenerator).generateReport(eq(expectedFileName), eq(statistic));
            }
        }
    }

    @Test
    void testGenerateReportsWithAsciiDocFormat() throws IOException {
        LogStatistics statistic = new LogStatistics(new LogFilter(), "source");

        LocalDateTime fixedTime = LocalDateTime.of(2024, 11, 30, 15, 10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM_HH-mm-ss-SSS");

        try (var mockedStaticTime = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS)) {
            mockedStaticTime.when(LocalDateTime::now).thenReturn(fixedTime);

            try (var mockedAsciiDoc = mockConstruction(ReportFormat.class)) {
                ReportManager.generateReports(List.of(statistic), Format.ADOC);

                String expectedFileName = "report_" + fixedTime.format(formatter) + Format.ADOC.label();

                List<ReportFormat> createdMocks = mockedAsciiDoc.constructed();
                assertEquals(1, createdMocks.size());

                ReportFormat mockGenerator = createdMocks.getFirst();
                verify(mockGenerator).generateReport(eq(expectedFileName), eq(statistic));
            }
        }
    }
}
