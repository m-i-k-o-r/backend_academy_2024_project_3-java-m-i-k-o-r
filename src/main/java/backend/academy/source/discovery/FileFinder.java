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

public class FileFinder {
    public static List<Path> findPaths(String path) {
        String searchLocation = path.split("(?<=^|/)(?=[^/])")[0];
        String globPattern = "glob:" + path;

        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(globPattern);
        List<Path> matchedPaths = new ArrayList<>();

        try {
            Files.walkFileTree(Paths.get(searchLocation), new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    if (pathMatcher.matches(path)) {
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
            throw new IllegalArgumentException("Ошибка при обходе дерева файлов: " + e.getMessage());
        }

        return matchedPaths;
    }
}
