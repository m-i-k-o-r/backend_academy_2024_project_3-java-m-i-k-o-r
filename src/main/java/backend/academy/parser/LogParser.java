package backend.academy.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "(?<remoteAddr>\\S+) " +
            "(?<remoteUser>\\S+) " +
            "- \\[(?<timeLocal>.*?)\\] " +
            "\"(?<request>.*?)\" " +
            "(?<status>\\d{3}) " +
            "(?<bodyBytesSent>\\d+) " +
            "\"(?<httpReferer>.*?)\" " +
            "\"(?<httpUserAgent>.*?)\""
    );

    private static final Pattern REQUEST_PATTERN = Pattern.compile("(?<method>\\S+) (?<path>\\S+) (?<protocol>\\S+)");

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");

    public static Optional<LogRecord> fromLogLine(String logLine) {
        Matcher matcher = LOG_PATTERN.matcher(logLine);
        if (!matcher.matches()) {
            System.err.println(" ! Строка лога не соответствует ожидаемому формату: " + logLine);
            return Optional.empty();
        }

        Optional<HttpRequest> request = parseRequest(matcher.group("request"));
        if (request.isEmpty()) {
            System.err.println(" ! Некорректный запрос в строке лога: " + matcher.group("request"));
            return Optional.empty();
        }

        return Optional.of(new LogRecord(
            matcher.group("remoteAddr"),
            matcher.group("remoteUser"),
            LocalDateTime.parse(matcher.group("timeLocal"), DATE_TIME_FORMATTER),
            request.get(),
            Integer.parseInt(matcher.group("status")),
            Integer.parseInt(matcher.group("bodyBytesSent")),
            matcher.group("httpReferer"),
            matcher.group("httpUserAgent")
        ));
    }

    public static Optional<HttpRequest> parseRequest(String requestString) {
        Matcher matcher = REQUEST_PATTERN.matcher(requestString);
        if (!matcher.matches()) {
            System.err.println(" ! Некорректный формат запроса: " + requestString);
            return Optional.empty();
        }

        return Optional.of(new HttpRequest(
            matcher.group("method"),
            matcher.group("path"),
            matcher.group("protocol")
        ));
    }
}
