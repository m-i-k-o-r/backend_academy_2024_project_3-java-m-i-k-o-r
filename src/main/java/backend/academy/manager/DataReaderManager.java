package backend.academy.manager;

import backend.academy.source.discovery.FileFinder;
import backend.academy.source.discovery.InputTypeDetector;
import backend.academy.source.reader.DataReader;
import backend.academy.source.reader.FileDataReader;
import backend.academy.source.reader.UrlDataReader;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Утилитный класс для управления процессом создания объектов {@link DataReader}
 * <hr>
 * Отвечает за выполнение следующих задач:
 * <ul>
 *     <li>создание объектов {@link DataReader} для различных типов путей {@link InputTypeDetector.InputType}:
 *         <ul>
 *             <li>{@link InputTypeDetector.InputType#URL} - для <b>URL</b></li>
 *             <li>{@link InputTypeDetector.InputType#PATH} - для <b>локальных файлов</b></li>
 *         </ul>
 *     </li>
 *     <li>обработка списка путей и создание соответствующих объектов {@link DataReader}</li>
 *     <li>логирование ошибок при создании {@link DataReader}</li>
 * </ul>
 */
@UtilityClass
public class DataReaderManager {
    private static final Logger LOGGER = LogManager.getLogger(DataReaderManager.class);

    /**
     * Создает список {@link DataReader} для заданных путей
     * <hr>
     * Для каждого пути из списка определяется его тип {@link InputTypeDetector.InputType} и
     * в зависимости от типа пути создается соответствующий {@link DataReader}
     *
     * @param paths список строк, представляющих пути
     * @return список {@link DataReader}, созданных для каждого пути
     */
    public static List<DataReader> createDataReaders(List<String> paths) {
        return paths.stream()
            .flatMap(DataReaderManager::processPath)
            .toList();
    }

    /**
     * Обрабатывает путь, определяя его тип {@link InputTypeDetector.InputType} и
     * создавая соответствующий {@link DataReader}
     *
     * @param path путь для обработки
     * @return поток объектов {@link DataReader}
     */
    private static Stream<DataReader> processPath(String path) {
        return switch (InputTypeDetector.identify(path)) {
            case URL -> createUrlDataReader(path);
            case PATH -> createFileDataReader(path);
            default -> {
                LOGGER.error("Некорректный путь: {}", path);
                yield Stream.empty();
            }
        };
    }

    /**
     * Создает {@link DataReader} для {@link InputTypeDetector.InputType#URL}
     *
     * @param path путь, представляющий URL
     * @return поток с объектом {@link UrlDataReader}, если URL правильный, или пустой поток в случае ошибки
     */
    private static Stream<DataReader> createUrlDataReader(String path) {
        try {
            return Stream.of(new UrlDataReader(URI.create(path).toURL()));
        } catch (Exception e) {
            LOGGER.error("Ошибка при создании UrlDataReader для пути: {}", path, e);
            return Stream.empty();
        }
    }

    /**
     * Создает {@link DataReader} для {@link InputTypeDetector.InputType#PATH}
     *
     * @param path путь, указывающий на файлы
     * @return поток объектов {@link FileDataReader} для каждого найденного файла
     */
    private static Stream<DataReader> createFileDataReader(String path) {
        try {
            List<Path> matchedPaths = FileFinder.findPaths(path);
            LOGGER.debug("Обнаружено файлов: {}", matchedPaths.size());
            return matchedPaths.stream().map(FileDataReader::new);
        } catch (Exception e) {
            LOGGER.error("Ошибка при создании FileDataReader для пути: {}", path, e);
            return Stream.empty();
        }
    }
}
