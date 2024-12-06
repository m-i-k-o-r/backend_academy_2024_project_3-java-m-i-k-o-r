package backend.academy.source.discovery;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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

/**
 * Утилитарный класс для поиска файлов и директорий по указанному пути и шаблону
 * <br>
 * Этот класс предоставляет методы для поиска файлов с использованием GLOB выражений
 */
@UtilityClass
public class FileFinder {

    /**
     * <b>!!! Данный метод предназначен только для тестов !!!</b>
     * <hr>
     * Ищет пути файлов, соответствующих шаблону, начиная с указанной директории
     *
     * @param basePath базовая директория для поиска
     * @param pattern  GLOB шаблон для поиска файлов
     * @return список путей, которые соответствуют переданному шаблону
     * @throws IOException если возникает ошибка при доступе к файловой системе
     */
    @SuppressFBWarnings
    public static List<Path> findPaths(String basePath, String pattern) throws IOException {
        Path searchLocation = Paths.get(basePath).toRealPath();

        return findPathsInDirectory(searchLocation, pattern);
    }

    /**
     * Ищет пути файлов, соответствующих шаблону, начиная с текущей директории
     *
     * @param pattern GLOB шаблон для поиска файлов
     * @return список путей, которые соответствуют переданному шаблону
     * @throws IOException если возникает ошибка при доступе к файловой системе
     */
    public static List<Path> findPaths(String pattern) throws IOException {
        Path searchLocation = Paths.get("").toRealPath();

        return findPathsInDirectory(searchLocation, pattern);
    }

    /**
     * Вспомогательный метод для поиска файлов, соответствующих шаблону, начиная с <b>указанной</b> директории
     *
     * @param searchLocation директория для поиска
     * @param pattern        GLOB шаблон
     * @return список путей, которые соответствуют переданному шаблону
     */
    private static List<Path> findPathsInDirectory(Path searchLocation, String pattern) {
        String globPattern = "glob:" + pattern;
        List<Path> matchedPaths = new ArrayList<>();

        PathMatcher pathMatcher;
        try {
            pathMatcher = FileSystems.getDefault().getPathMatcher(globPattern);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Некорректный шаблон пути: " + globPattern, e);
        }

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
