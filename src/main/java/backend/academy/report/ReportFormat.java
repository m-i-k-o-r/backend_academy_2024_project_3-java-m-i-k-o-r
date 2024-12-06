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

/**
 * Класс для создания отчета в заданном формате {@link ReportGenerator}
 * <hr>
 * Предоставляет возможность генерировать отчеты на основе статистики {@link LogStatistics}
 * и представлять их в различных форматах:
 * <ul>
 *      <li>Markdown</li>
 *      <li>AsciiDoc</li>
 * </ul>
 * <hr>
 * Содержит набор секций {@link #sections}, которые формируют структуру отчета
 */
public class ReportFormat {

    /**
     * Формат генерации отчета, реализующий интерфейс {@link ReportGenerator}
     */
    private final ReportGenerator format;

    /**
     * Список секций, которые будут добавлены в отчет
     * <br>
     * Каждая отдельная секция отвечает за отображение определенного информационного блока
     */
    private final List<Section> sections;

    /**
     * Конструктор класса {@link ReportFormat}
     * <hr>
     * Создаёт объект с заданным форматом отчета и предопределенным набором секций:
     * <ul>
     *   <li>Общая информация</li>
     *   <li>Фильтры</li>
     *   <li>Запрашиваемые ресурсы</li>
     *   <li>Коды ответа</li>
     *   <li>Методы запросов</li>
     *   <li>Самые активные пользователи</li>
     *   <li>Дневная активность</li>
     * </ul>
     * <hr>
     *
     * @param format {@link ReportGenerator} формат генерации отчета
     */
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

    /**
     * Генерирует отчет и сохраняет его в файл
     * <hr>
     * Метод проходит по всем секциям и вызывает
     * их метод {@link Section#write(BufferedWriter, LogStatistics, ReportGenerator)}
     * для записи данных в указанный файл
     *
     * @param filename   имя файла, в который будет записан отчет
     * @param statistics {@link LogStatistics}, содержащий данные для отчета
     * @throws IOException если происходит ошибка при записи в файл
     */
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
