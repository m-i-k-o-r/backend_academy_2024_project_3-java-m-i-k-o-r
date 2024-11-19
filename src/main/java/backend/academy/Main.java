package backend.academy;

import backend.academy.cli.CliParams;
import backend.academy.cli.CliParser;
import backend.academy.filter.LogFilter;
import backend.academy.report.AsciiDocReportGenerator;
import backend.academy.report.MarkdownReportGenerator;
import backend.academy.source.discovery.FileFinder;
import backend.academy.source.discovery.InputTypeDetector;
import backend.academy.source.file.DataReader;
import backend.academy.source.file.FileDataReader;
import backend.academy.source.file.UrlDataReader;
import backend.academy.statistics.LogStatistics;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {

    public static void main(String[] args) throws IOException {

        // String input = "./**.md";
        // String input = "C:/Users/korol/Desktop/lol/New Text Document.txt";
        // String input = "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs";

        CliParser cliParser = new CliParser();
        cliParser.parseArgs(args);

        CliParams params = cliParser.cliParams();

        List<DataReader> dataReaderList = new ArrayList<>();

        for (String s : params.paths()) {
            switch (InputTypeDetector.identify(s)) {
                case URL:
                    dataReaderList.add(new UrlDataReader(URI.create(s).toURL()));
                    break;
                case PATH:
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

        for (LogStatistics statistic : statistics) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM_HH-mm-ss-SSS"));

            String reportFileName = "report_";
            if (params.format().equals("markdown")) {
                reportFileName = reportFileName + timestamp + ".md";
                new MarkdownReportGenerator().generateReport(reportFileName, statistic);
            } else if (params.format().equals("adoc")) {
                reportFileName = reportFileName + timestamp + ".adoc";
                new AsciiDocReportGenerator().generateReport(reportFileName, statistic);
            }
        }
    }
}
