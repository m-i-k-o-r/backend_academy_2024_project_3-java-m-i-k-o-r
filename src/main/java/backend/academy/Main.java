package backend.academy;

import backend.academy.file.DataReader;
import backend.academy.file.FileDataReader;
import backend.academy.file.FileFinder;
import backend.academy.file.UrlDataReader;
import backend.academy.statistics.LogStatistics;
import lombok.experimental.UtilityClass;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

@UtilityClass
public class Main {
    public static void main(String[] args) throws IOException {

        String input = "...";

        DataReader dr = null;

        switch (InputTypeDetector.identify(input)) {
            case URL:
                dr = new UrlDataReader(URI.create(input).toURL());
                break;
            case PATH:
                List<Path> matchedPaths = FileFinder.findPaths(input);
                dr = new FileDataReader(matchedPaths.getFirst());
                break;
            case INVALID:
                System.err.println(" ! Некорректный ввод. Пожалуйста, укажите корректный URL или путь.");
                break;
        }

        if (dr != null) {
            LogStatistics statistics = dr.read();
        } else {
            System.err.println(" ! Ошибка: Не удалось инициализировать DataReader.");
        }
    }
}
