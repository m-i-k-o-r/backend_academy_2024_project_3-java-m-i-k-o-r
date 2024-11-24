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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataReaderManager {
    private static final Logger logger = LogManager.getLogger(DataReaderManager.class);

    public static List<DataReader> createDataReaders(List<String> paths) {
        return paths.stream()
            .flatMap(path -> {
                switch (InputTypeDetector.identify(path)) {
                    case URL:
                        try {
                            return Stream.of(new UrlDataReader(URI.create(path).toURL()));
                        } catch (Exception e) {
                            logger.error("Ошибка при создании UrlDataReader для пути: {}", path, e);
                            return Stream.empty();
                        }
                    case PATH:
                        try {
                            List<Path> matchedPaths = FileFinder.findPaths(path);
                            return matchedPaths.stream().map(FileDataReader::new);
                        } catch (Exception e) {
                            logger.error("Ошибка при создании FileDataReader для пути: {}", path, e);
                            return Stream.empty();
                        }
                    default:
                        logger.error("Некорректный путь: {}", path);
                        return Stream.empty();
                }
            })
            .toList();
    }
}
