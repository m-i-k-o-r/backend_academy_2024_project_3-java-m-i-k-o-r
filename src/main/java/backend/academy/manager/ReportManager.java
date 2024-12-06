package backend.academy.manager;

import backend.academy.cli.Format;
import backend.academy.report.AsciiDocReportGenerator;
import backend.academy.report.MarkdownReportGenerator;
import backend.academy.report.ReportFormat;
import backend.academy.statistics.LogStatistics;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static backend.academy.utils.Constants.EXCLUSION_DETAILS;

/**
 * Утилитный класс для управления процессом создания отчетов
 * <hr>
 * Отвечает за выполнение следующих задач:
 * <ul>
 *     <li>генерацию отчетов для списка статистики логов:
 *         <ul>
 *             <li>в формате <b>Markdown</b></li>
 *             <li>в формате <b>AsciiDoc</b></li>
 *         </ul>
 *     </li>
 *     <li>создает отчет для каждого объекта статистики</li>
 *     <li>сохраняет отчет</li>
 * </ul>
 */
@UtilityClass
public class ReportManager {
    private static final Logger LOGGER = LogManager.getLogger(ReportManager.class);

    /**
     * Метод принимает список объектов статистики и формат отчета,
     * создает отчет для каждого объекта статистики и сохраняет его
     * в файл с названием, которое включает в себя временную метку для уникальности
     * <hr>
     * Каждый отчет генерируется с использованием соответствующего генератора:
     * <ul>
     *     <li>{@link MarkdownReportGenerator} для формата Markdown {@link Format#MARKDOWN}</li>
     *     <li>{@link AsciiDocReportGenerator} для формата AsciiDoc {@link Format#ADOC}</li>
     * </ul>
     * <hr>
     * <b>Если при генерации отчета возникает ошибка, она логируется с предупреждением и
     * подробностями, включая источник данных.
     * После этого программа прекращает запись в текущий файл и переходит к следующей статистике</b>
     * <hr>
     *
     * @param statistics список объектов {@link LogStatistics}, содержащие статистику для создания отчетов
     * @param format     формат отчета, {@link Format#MARKDOWN} или {@link Format#ADOC}
     */
    public static void generateReports(List<LogStatistics> statistics, Format format) {
        for (LogStatistics statistic : statistics) {
            try {
                ReportFormat report;
                switch (format) {
                    case MARKDOWN: {
                        report = new ReportFormat(new MarkdownReportGenerator());
                        break;
                    }
                    case ADOC: {
                        report = new ReportFormat(new AsciiDocReportGenerator());
                        break;
                    }
                    default: {
                        throw new IOException("Неподдерживаемый формат отчета: " + format);
                    }
                }
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM_HH-mm-ss-SSS"));
                String reportFileName = "report_" + timestamp + format.label();
                report.generateReport(reportFileName, statistic);

                LOGGER.info("Создание отчета успешно завершено. "
                    + "Файл: '{}'. Источник: '{}'", reportFileName, statistic.source());

            } catch (IOException e) {
                LOGGER.warn("Ошибка при генерации отчета: {}", statistic.source());
                LOGGER.debug(EXCLUSION_DETAILS, e);
            }
        }
    }
}
