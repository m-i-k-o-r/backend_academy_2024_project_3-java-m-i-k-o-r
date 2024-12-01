package backend.academy.manager;

import backend.academy.source.discovery.FileFinder;
import backend.academy.source.discovery.InputTypeDetector;
import backend.academy.source.reader.DataReader;
import backend.academy.source.reader.FileDataReader;
import backend.academy.source.reader.UrlDataReader;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

public class DataReaderManagerTest {
    private static final String URL_PATH = "http://example.lol/data";
    private static final String FILE_PATH = "data/file.txt";
    private static final String INVALID_PATH = "invalid_path";

    @Test
    void testCreateDataReadersWithUrlPath() {
        try (MockedStatic<InputTypeDetector> detectorMock = mockStatic(InputTypeDetector.class)) {

            detectorMock.when(() -> InputTypeDetector.identify(URL_PATH))
                .thenReturn(InputTypeDetector.InputType.URL);

            List<DataReader> readers = DataReaderManager.createDataReaders(List.of(URL_PATH));

            assertEquals(1, readers.size());
            assertInstanceOf(UrlDataReader.class, readers.getFirst());
            assertEquals(URI.create(URL_PATH), URI.create(readers.getFirst().getSource()));
        }
    }

    @Test
    void testCreateDataReadersWithFilePath() {
        try (MockedStatic<InputTypeDetector> detectorMock = mockStatic(InputTypeDetector.class);
             MockedStatic<FileFinder> fileFinderMock = mockStatic(FileFinder.class)) {

            detectorMock.when(() -> InputTypeDetector.identify(FILE_PATH))
                .thenReturn(InputTypeDetector.InputType.PATH);

            Path mockPath = Path.of(FILE_PATH);

            fileFinderMock.when(() -> FileFinder.findPaths(FILE_PATH))
                .thenReturn(List.of(mockPath));

            List<DataReader> readers = DataReaderManager.createDataReaders(List.of(FILE_PATH));

            assertEquals(1, readers.size());
            assertInstanceOf(FileDataReader.class, readers.getFirst());
            assertEquals(mockPath.toAbsolutePath(), Path.of(URI.create(readers.getFirst().getSource())));
        }
    }

    @Test
    void testCreateDataReadersWithInvalidPath() {
        try (MockedStatic<InputTypeDetector> detectorMock = mockStatic(InputTypeDetector.class)) {

            detectorMock.when(() -> InputTypeDetector.identify(INVALID_PATH))
                .thenReturn(InputTypeDetector.InputType.INVALID);

            List<DataReader> readers = DataReaderManager.createDataReaders(List.of(INVALID_PATH));

            assertTrue(readers.isEmpty());
        }
    }

    @Test
    void testUrlDataReaderCreationException() {
        String malformedUrl = "htp://invalid-url-lol";
        try (MockedStatic<InputTypeDetector> detectorMock = mockStatic(InputTypeDetector.class)) {

            detectorMock.when(() -> InputTypeDetector.identify(malformedUrl))
                .thenReturn(InputTypeDetector.InputType.URL);

            List<DataReader> readers = DataReaderManager.createDataReaders(List.of(malformedUrl));

            assertTrue(readers.isEmpty());
        }
    }

    @Test
    void testFileDataReaderCreationException() {
        try (MockedStatic<InputTypeDetector> detectorMock = mockStatic(InputTypeDetector.class);
             MockedStatic<FileFinder> fileFinderMock = mockStatic(FileFinder.class)) {

            detectorMock.when(() -> InputTypeDetector.identify(FILE_PATH))
                .thenReturn(InputTypeDetector.InputType.PATH);

            fileFinderMock.when(() -> FileFinder.findPaths(FILE_PATH))
                .thenThrow(new RuntimeException());

            List<DataReader> readers = DataReaderManager.createDataReaders(List.of(FILE_PATH));

            assertTrue(readers.isEmpty());
        }
    }
}
