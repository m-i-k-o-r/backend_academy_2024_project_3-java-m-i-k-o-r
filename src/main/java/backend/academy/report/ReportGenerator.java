package backend.academy.report;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Интерфейс для генерации отчетов
 * <hr>
 * Содержит методы для записи различных частей отчета:
 * <ul>
 *      <li>заголовков</li>
 *      <li>таблиц</li>
 *      <li>информационного текста</li>
 * </ul>
 */
public interface ReportGenerator {

    /**
     * Записывает заголовок отчета
     *
     * @param writer {@link BufferedWriter} поток для записи данных в файл
     * @param header заголовок отчета
     * @throws IOException если произошла ошибка при записи
     */
    void writeHeader(BufferedWriter writer, String header) throws IOException;

    /**
     * Записывает информационную строку
     *
     * @param writer {@link BufferedWriter} поток для записи данных в файл
     * @param cells  массив строк, содержащих информацию для записи
     * @throws IOException если произошла ошибка при записи в файл
     */
    void writeInfo(BufferedWriter writer, String... cells) throws IOException;

    /**
     * Записывает заголовок таблицы
     *
     * @param writer  {@link BufferedWriter} поток для записи данных в файл
     * @param headers массив строк, содержащих заголовки колонок
     * @throws IOException если произошла ошибка при записи в файл
     */
    void writeTableHeader(BufferedWriter writer, String... headers) throws IOException;

    /**
     * Записывает строку таблицы
     *
     * @param writer {@link BufferedWriter} поток для записи данных в файл
     * @param cells  массив строк, содержащих данные для записи в таблицу
     * @throws IOException если произошла ошибка при записи в файл
     */
    void writeTableRow(BufferedWriter writer, String... cells) throws IOException;

    /**
     * Завершает запись таблицы
     *
     * @param writer {@link BufferedWriter} поток для записи данных в файл
     * @throws IOException если произошла ошибка при записи в файл
     */
    void writeTableEnd(BufferedWriter writer) throws IOException;
}
