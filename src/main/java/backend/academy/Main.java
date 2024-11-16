package backend.academy;

import backend.academy.source.file.DataReader;
import backend.academy.source.file.FileDataReader;
import backend.academy.source.discovery.FileFinder;
import backend.academy.source.discovery.InputTypeDetector;
import backend.academy.source.file.UrlDataReader;
import backend.academy.filter.LogFilter;
import backend.academy.statistics.LogStatistics;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {

    public static void main(String[] args) throws IOException {

        // String input = "./**.md";
        // String input = "C:/Users/korol/Desktop/lol/New Text Document.txt";

        String input = "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs";

        List<String> inputs = new ArrayList<>();
        List<DataReader> dataReaderList = new ArrayList<>();
        inputs.add(input);

        for (String s : inputs) {
            switch (InputTypeDetector.identify(s)) {
                case URL:
                    System.out.println("Обнаружен URL");
                    dataReaderList.add(new UrlDataReader(URI.create(s).toURL()));
                    break;
                case PATH:
                    System.out.println("Обнаружен путь с glob-шаблоном");
                    List<Path> matchedPaths = FileFinder.findPaths(s);
                    for (Path p : matchedPaths) {
                        dataReaderList.add(new FileDataReader(p));
                    }
                    break;
                case INVALID:
                    System.err.println(" ! Некорректный ввод. Пожалуйста, укажите корректный URL или путь.");
                    break;
            }
        }

        LogFilter filters = new LogFilter();
        List<LogStatistics> statistics = new ArrayList<>();
        for (DataReader dataReader : dataReaderList) {
            statistics.add(dataReader.read(filters));
        }
    }
}
