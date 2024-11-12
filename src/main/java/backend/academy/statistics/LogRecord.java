package backend.academy.statistics;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record LogRecord(
    String remoteAddr,
    String remoteUser,
    LocalDateTime timeLocal,
    String request,
    int status,
    int bodyBytesSent,
    String httpReferer,
    String httpUserAgent
) {
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

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");

    public static Optional<LogRecord> fromLogLine(String logLine) {
        Matcher matcher = LOG_PATTERN.matcher(logLine);
        if (!matcher.matches()) {
            System.err.println(" ! Строка лога не соответствует ожидаемому формату: " + logLine);
            return Optional.empty();
        }
        return Optional.of(new LogRecord(
            matcher.group("remoteAddr"),
            matcher.group("remoteUser"),
            LocalDateTime.parse(matcher.group("timeLocal"), DATE_TIME_FORMATTER),
            matcher.group("request"),
            Integer.parseInt(matcher.group("status")),
            Integer.parseInt(matcher.group("bodyBytesSent")),
            matcher.group("httpReferer"),
            matcher.group("httpUserAgent")
        ));
    }
}
