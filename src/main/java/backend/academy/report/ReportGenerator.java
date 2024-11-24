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
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ReportGenerator {
    private static final Logger logger = LogManager.getLogger(ReportGenerator.class);

    private final List<Section> sections = List.of(
        new GeneralInfoSection(),
        new FilterSection(),
        new RequestedResourcesSection(),
        new ResponseCodesSection(),
        new RequestMethodsSection(),
        new UserActivitySection(),
        new MostActiveDaySection()
    );

    public void generateReport(String filename, LogStatistics statistics) throws IllegalAccessException {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(filename), StandardCharsets.UTF_8)) {
            for (Section section : sections) {
                section.write(writer, statistics, this);
            }
            logger.info("Создание отчета успешно завершено. "
                + "Файл: '{}', Источник: {}", filename, statistics.source());
        } catch (IOException e) {
            logger.error("Ошибка при создании отчета: {} Произошла ошибка: {}", filename, e.getMessage());
            logger.debug("Детали исключения:", e);
        }
    }

    public abstract void writeHeader(BufferedWriter writer, String header) throws IOException;

    public void writeInfo(BufferedWriter writer, String... headers) throws IOException {
        if (headers.length >= 2) {
            String value = String.join(", ", Arrays.copyOfRange(headers, 1, headers.length));
            writer.write(String.format("**%s**: %s\n\n", headers[0], value));
        } else {
            writer.write(String.format("**%s**: -\n\n", headers[0]));
        }
    }

    public abstract void writeTableHeader(BufferedWriter writer, String... headers) throws IOException;

    public abstract void writeTableRow(BufferedWriter writer, String... cells) throws IOException;

    public abstract void writeTableEnd(BufferedWriter writer) throws IOException;
}
