package backend.academy.sourse;

import backend.academy.source.discovery.FileFinder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileFinderTest {

    @TempDir
    Path tempDir;

    @Test
    void testFindPathsWithMatchingFiles() throws IOException {
        Path file1 = Files.createFile(tempDir.resolve("file1.txt"));
        Path file2 = Files.createFile(tempDir.resolve("file2.txt"));
        Files.createFile(tempDir.resolve("not-a-match.log"));

        List<Path> result = FileFinder.findPaths(tempDir.toString(), "*.txt");

        assertEquals(2, result.size());
        assertTrue(result.contains(file1));
        assertTrue(result.contains(file2));
    }

    @Test
    void testFindPathsNoMatches() throws IOException {
        Files.createFile(tempDir.resolve("file1.log"));
        Files.createFile(tempDir.resolve("file2.log"));

        List<Path> result = FileFinder.findPaths(tempDir.toString(), "*.txt");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindPathsInvalidGlobPattern() {
        String invalidPattern = "[*.txt";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> FileFinder.findPaths(tempDir.toString(), invalidPattern));

        assertTrue(exception.getMessage().contains("Некорректный шаблон пути"));
    }
}
