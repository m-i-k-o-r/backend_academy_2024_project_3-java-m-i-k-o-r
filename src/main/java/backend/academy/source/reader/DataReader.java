package backend.academy.source.reader;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogRecord;
import backend.academy.parser.LogParser;
import backend.academy.statistics.LogStatistics;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Абстрактный класс для чтения данных и анализа логов
 * <br>
 * Этот класс предоставляет механизм для создания ридера, получения источника данных
 * и чтения логов с обновлением статистики
 */
public abstract class DataReader {

    /**
     * Создает ридер для чтения данных из источника
     *
     * @return объект {@link BufferedReader} для чтения данных
     * @throws IOException если возникает ошибка при создании ридера
     */
    protected abstract BufferedReader createReader() throws IOException;

    /**
     * Возвращает строковое представление источника
     *
     * @return строка, являющаяся источником данных
     */
    public abstract String getSource();

    /**
     * Читает данные из источника, фильтрует их и обновляет статистику
     *
     * @param filter фильтр
     * @return объект {@link LogStatistics}, который собирает статистику
     * @throws RuntimeException если возникает ошибка при чтении данных из источника
     */
    public LogStatistics read(LogFilter filter) throws RuntimeException {
        LogStatistics statistics = new LogStatistics(filter, getSource());
        try (BufferedReader in = createReader()) {
            String line;
            while ((line = in.readLine()) != null) {
                LogRecord entry = LogParser.parseLogLine(line);

                statistics.update(entry);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении данных из источника: " + getSource(), e);
        }
        return statistics;
    }
}
