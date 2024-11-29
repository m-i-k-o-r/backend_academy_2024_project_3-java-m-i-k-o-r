package backend.academy.source.discovery;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileFinder {
    public static List<Path> findPaths(String path) throws IOException {
        Path searchLocation = Paths.get("").toRealPath();
        String globPattern = "glob:" + path;

        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(globPattern);
        List<Path> matchedPaths = new ArrayList<>();

        try {
            Files.walkFileTree(searchLocation, new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    Path relativePath = searchLocation.relativize(path);
                    if (pathMatcher.matches(relativePath)) {
                        matchedPaths.add(path);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка при обходе дерева файлов: ", e);
        }

        return matchedPaths;
    }
}
