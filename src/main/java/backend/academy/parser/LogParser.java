package backend.academy.parser;

import backend.academy.model.HttpRequest;
import backend.academy.model.LogRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/**
 * Утилитарный класс для парсинга строк лога
 * <br>
 * Этот класс разбирает и преобразовывает строку лога в объекты {@link LogRecord} и {@link HttpRequest}
 */
@UtilityClass
public class LogParser {

    /** Шаблон для разбора строки лога */
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

    /** Шаблон для разбора строки HTTP-запроса */
    private static final Pattern REQUEST_PATTERN = Pattern.compile("(?<method>\\S+) (?<path>\\S+) (?<protocol>\\S+)");

    /** Форматтер для разбора даты из лога */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");

    /**
     * Парсит строку лога и преобразует её в объект {@link LogRecord}
     *
     * @param logLine строка лога для разбора
     * @return объект {@link LogRecord}
     * @throws IllegalArgumentException если строка не соответствует шаблону
     */
    public static LogRecord parseLogLine(String logLine) throws IllegalArgumentException {
        Matcher matcher = parseWithPattern(LOG_PATTERN, logLine);

        return new LogRecord(
            matcher.group("remoteAddr"),
            matcher.group("remoteUser"),
            parseDateTime(matcher.group("timeLocal")),
            parseHttpRequest(matcher.group("request")),
            Integer.parseInt(matcher.group("status")),
            Integer.parseInt(matcher.group("bodyBytesSent")),
            matcher.group("httpReferer"),
            matcher.group("httpUserAgent")
        );
    }

    /**
     * Выполняет разбор строки с использованием указанного шаблона
     *
     * @param pattern шаблон для разбора
     * @param input   строка для разбора
     * @return объект {@link Matcher}
     * @throws IllegalArgumentException если строка не соответствует шаблону
     */
    private static Matcher parseWithPattern(Pattern pattern, String input) throws IllegalArgumentException {
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return matcher;
        } else {
            throw new IllegalArgumentException("Не удалось сопоставить строку с паттерном");
        }
    }

    /**
     * Парсит строку HTTP-запроса и преобразует её в объект {@link HttpRequest}
     *
     * @param requestString строка запроса для разбора
     * @return объект {@link HttpRequest}
     * @throws IllegalArgumentException если строка не соответствует шаблону запроса
     */
    private static HttpRequest parseHttpRequest(String requestString) throws IllegalArgumentException {
        try {
            Matcher matcher = parseWithPattern(REQUEST_PATTERN, requestString);

            return new HttpRequest(
                matcher.group("method"),
                matcher.group("path"),
                matcher.group("protocol")
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ошибка при парсинге запроса: " + requestString, e);
        }
    }

    /**
     * Парсит строку даты и времени из лога
     *
     * @param dateTimeString строка с датой и временем
     * @return объект {@link LocalDateTime}
     * @throws IllegalArgumentException если строка не соответствует формату
     */
    private static LocalDateTime parseDateTime(String dateTimeString) throws IllegalArgumentException {
        try {
            return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ошибка при парсинге даты: " + dateTimeString, e);
        }
    }
}
