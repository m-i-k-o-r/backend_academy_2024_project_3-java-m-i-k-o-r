package backend.academy.manager;

import backend.academy.filter.LogFilter;
import backend.academy.source.reader.DataReader;
import backend.academy.statistics.LogStatistics;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для управления сбором статистики из различных источников данных
 * <hr>
 * Отвечает за выполнение следующих задач:
 * <ul>
 *     <li>чтение данных из указанных источников</li>
 *     <li>фильтрацию</li>
 *     <li>сбор статистики</li>
 *     <li>логирование процессов чтения и обработки данных</li>
 * </ul>
 */
@AllArgsConstructor
public class StatisticsManager {
    private final LogFilter filters;

    private static final Logger LOGGER = LogManager.getLogger(StatisticsManager.class);

    /**
     * Собирает статистику из списка источников данных с применением фильтров
     * <hr>
     * Метод выполняет:
     * <ul>
     *     <li>инициирование чтения данных из каждого источника, переданного в {@code dataReaders}</li>
     *     <li>применение фильтров {@link #filters}</li>
     *     <li>логирование событий:
     *         <ul>
     *             <li>начало чтения данных</li>
     *             <li>ошибки, возникающие при чтении, с указанием источника и причины</li>
     *             <li>успешное завершение чтения данных</li>
     *         </ul>
     *     </li>
     * </ul>
     * <hr>
     * <b>Если процесс чтения из источника завершается ошибкой, статистика из данного источника не включается
     * в результирующий список статистик</b>
     * <hr>
     *
     * @param dataReaders список объектов {@link DataReader}, отвечающих за чтение данных
     * @return список объектов {@link LogStatistics}, содержащих собранную и обработанную статистику
     */
    public List<LogStatistics> collectStatistics(List<DataReader> dataReaders) {
        return dataReaders.stream()
            .peek(dataReader -> LOGGER.info("Начало чтения данных из источника: '{}'", dataReader.getSource()))
            .map(dataReader -> {
                try {
                    return dataReader.read(filters);
                } catch (RuntimeException e) {
                    LOGGER.error("Ошибка при чтении данных из источника: {}. Причина: {}",
                        dataReader.getSource(), e.getMessage());

                    return null;
                }
            })
            .filter(Objects::nonNull)
            .peek(stats -> LOGGER.info("Завершено чтение данных из источника: '{}'", stats.source()))
            .toList();
    }
}
