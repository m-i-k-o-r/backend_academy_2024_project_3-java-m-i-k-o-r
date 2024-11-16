package backend.academy.file;

import java.io.IOException;
import java.nio.file.*;
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
            System.err.println(" ! Ошибка при обходе дерева файлов: " + e.getMessage());
            return new ArrayList<>();
        }

        return matchedPaths;
    }
}
