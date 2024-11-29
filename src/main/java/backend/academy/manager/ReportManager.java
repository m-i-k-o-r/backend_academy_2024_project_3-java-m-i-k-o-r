package backend.academy.manager;

import backend.academy.cli.Format;
import backend.academy.report.AsciiDocReportGenerator;
import backend.academy.report.MarkdownReportGenerator;
import backend.academy.statistics.LogStatistics;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static backend.academy.utils.Constants.EXCLUSION_DETAILS;

@UtilityClass
public class ReportManager {
    private static final Logger LOGGER = LogManager.getLogger(ReportManager.class);

    public static void generateReports(List<LogStatistics> statistics, Format format) {
        for (LogStatistics statistic : statistics) {
            try {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM_HH-mm-ss-SSS"));
                String reportFileName = "report_" + timestamp + format.label();

                switch (format) {
                    case MARKDOWN: {
                        new MarkdownReportGenerator().generateReport(reportFileName, statistic);
                        break;
                    }
                    case ADOC: {
                        new AsciiDocReportGenerator().generateReport(reportFileName, statistic);
                        break;
                    }
                    default: {
                        throw new IOException("Неподдерживаемый формат отчета: " + format);
                    }
                }

                LOGGER.info("Создание отчета успешно завершено. "
                    + "Файл: '{}'. Источник: '{}'", reportFileName, statistic.source());

            } catch (IOException e) {
                LOGGER.warn("Ошибка при генерации отчета: {}", statistic.source());
                LOGGER.debug(EXCLUSION_DETAILS, e);
            }
        }
    }
}
