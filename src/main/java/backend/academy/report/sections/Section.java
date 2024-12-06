package backend.academy.report.sections;

import backend.academy.report.ReportGenerator;
import backend.academy.statistics.LogStatistics;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 * Абстрактный класс для описания секции отчета
 * <hr>
 * Определяет общий шаблон для записи заголовков, таблиц и информации
 * с использованием {@link ReportGenerator}
 */
public abstract class Section {

    /**
     * Генерирует и записывает содержимое секции
     *
     * @param writer          {@link BufferedWriter} поток для записи данных
     * @param statistics      {@link LogStatistics} статистика, используемая для генерации содержимого
     * @param reportGenerator {@link ReportGenerator} генератор отчетов для форматирования вывода
     * @throws IOException если возникает ошибка при записи данных
     */
    public void write(
        BufferedWriter writer,
        LogStatistics statistics,
        ReportGenerator reportGenerator
    ) throws IOException {

        reportGenerator.writeHeader(writer, getHeader());

        for (List<String> row : prepareInfo(statistics)) {
            reportGenerator.writeInfo(writer, listToArray(row));
        }

        reportGenerator.writeTableHeader(writer, listToArray(getTableHeaders()));

        for (List<String> row : prepareRows(statistics)) {
            reportGenerator.writeTableRow(writer, listToArray(row));
        }

        reportGenerator.writeTableEnd(writer);
    }

    /**
     * Возвращает заголовок секции
     *
     * @return строка с заголовком секции
     */
    protected abstract String getHeader();

    /**
     * Возвращает заголовки столбцов для таблицы
     *
     * @return список заголовков столбцов таблицы
     */
    protected abstract List<String> getTableHeaders();

    /**
     * Возвращает строки данных для таблицы
     *
     * @param statistics статистика, используемая для подготовки данных
     * @return список строк, каждая из которых является списком ячеек
     */
    protected abstract List<List<String>> prepareRows(LogStatistics statistics);

    /**
     * Возвращает дополнительную информацию для записи перед таблицей
     * По умолчанию возвращает пустой список
     *
     * @param statistics статистика, используемая для подготовки данных
     * @return список строк, каждая из которых является списком ячеек
     */
    protected List<List<String>> prepareInfo(LogStatistics statistics) {
        return List.of();
    }

    /**
     * Конвертирует список строк в массив строк
     *
     * @param list список строк
     * @return массив строк
     */
    private String[] listToArray(List<String> list) {
        return list.toArray(new String[0]);
    }
}
