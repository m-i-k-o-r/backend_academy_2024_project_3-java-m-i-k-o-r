package backend.academy.parser;

import backend.academy.model.HttpRequest;
import backend.academy.model.LogRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogParser {
    private static final Logger logger = LogManager.getLogger(LogParser.class);

    private static final Pattern LOG_PATTERN = Pattern.compile(
        "(?<remoteAddr>\\S+) "
            + "(?<remoteUser>\\S+) "
            + "- \\[(?<timeLocal>.*?)\\] "
            + "\"(?<request>.*?)\" "
            + "(?<status>\\d{3}) "
            + "(?<bodyBytesSent>\\d+) "
            + "\"(?<httpReferer>.*?)\" "
            + "\"(?<httpUserAgent>.*?)\""
    );

    private static final Pattern REQUEST_PATTERN = Pattern.compile("(?<method>\\S+) (?<path>\\S+) (?<protocol>\\S+)");

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");

    public static Optional<LogRecord> parseLogLine(String logLine) {
        try {
            return parseWithPattern(LOG_PATTERN, logLine)
                .flatMap(matcher -> {
                    String remoteAddr = matcher.group("remoteAddr");
                    String remoteUser = matcher.group("remoteUser");
                    String timeLocalStr = matcher.group("timeLocal");
                    String requestStr = matcher.group("request");
                    String statusStr = matcher.group("status");
                    String bodyBytesSentStr = matcher.group("bodyBytesSent");
                    String httpReferer = matcher.group("httpReferer");
                    String httpUserAgent = matcher.group("httpUserAgent");

                    Optional<HttpRequest> request = parseHttpRequest(requestStr);
                    if (request.isEmpty()) {
                        logger.error("Ошибка при парсинге запроса: {}", requestStr);
                        return Optional.empty();
                    }

                    Optional<LocalDateTime> timeLocal = parseDateTime(timeLocalStr);
                    if (timeLocal.isEmpty()) {
                        logger.error("Ошибка при парсинге даты: {}", timeLocalStr);
                        return Optional.empty();
                    }

                    int status = Integer.parseInt(statusStr);
                    int bodyBytesSent = Integer.parseInt(bodyBytesSentStr);

                    return Optional.of(new LogRecord(
                        remoteAddr,
                        remoteUser,
                        timeLocal.get(),
                        request.get(),
                        status,
                        bodyBytesSent,
                        httpReferer,
                        httpUserAgent
                    ));
                });
        } catch (Exception e) {
            logger.error("Ошибка при парсинге строки лога: {}. Причина: {}", logLine, e.getMessage());
            return Optional.empty();
        }
    }

    private static Optional<Matcher> parseWithPattern(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return Optional.of(matcher);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<HttpRequest> parseHttpRequest(String requestString) {
        Optional<Matcher> matcherOpt = parseWithPattern(REQUEST_PATTERN, requestString);
        if (matcherOpt.isEmpty()) {
            return Optional.empty();
        }

        Matcher matcher = matcherOpt.get();
        return Optional.of(new HttpRequest(
            matcher.group("method"),
            matcher.group("path"),
            matcher.group("protocol")
        ));
    }

    private static Optional<LocalDateTime> parseDateTime(String dateTimeString) {
        try {
            return Optional.of(LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}
