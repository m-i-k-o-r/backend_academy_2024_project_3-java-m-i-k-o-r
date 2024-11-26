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

@UtilityClass
public class DataReaderManager {
    private static final Logger LOGGER = LogManager.getLogger(DataReaderManager.class);

    public static List<DataReader> createDataReaders(List<String> paths) {
        return paths.stream()
            .flatMap(DataReaderManager::processPath)
            .toList();
    }

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

    private static Stream<DataReader> createUrlDataReader(String path) {
        try {
            return Stream.of(new UrlDataReader(URI.create(path).toURL()));
        } catch (Exception e) {
            LOGGER.error("Ошибка при создании UrlDataReader для пути: {}", path, e);
            return Stream.empty();
        }
    }

    private static Stream<DataReader> createFileDataReader(String path) {
        try {
            List<Path> matchedPaths = FileFinder.findPaths(path);
            return matchedPaths.stream().map(FileDataReader::new);
        } catch (Exception e) {
            LOGGER.error("Ошибка при создании FileDataReader для пути: {}", path, e);
            return Stream.empty();
        }
    }
}
