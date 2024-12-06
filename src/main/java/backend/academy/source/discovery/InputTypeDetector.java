package backend.academy.source.discovery;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static backend.academy.utils.Constants.HTTP;
import static backend.academy.utils.Constants.HTTPS;

/**
 * Утилитарный класс для определения типа входных данных
 * <br>
 * Этот класс позволяет определить, является ли входная строка URL, путем или она недействительна
 */
@UtilityClass
public class InputTypeDetector {

    /**
     * Типы входных данных
     */
    public enum InputType {
        URL,
        PATH,
        INVALID
    }

    private static final Logger LOGGER = LogManager.getLogger(InputTypeDetector.class);

    /**
     * Определяет тип входной строки
     *
     * @param input входная строка
     * @return тип входных данных: URL, PATH или INVALID
     */
    public static InputType identify(String input) {
        if (isValidUrl(input)) {
            LOGGER.info("Ввод распознан как URL: '{}'", input);
            return InputType.URL;
        }
        if (isValidPath(input)) {
            LOGGER.info("Ввод распознан как PATH: '{}'", input);
            return InputType.PATH;
        }
        LOGGER.warn("Ввод не распознан как PATH или URL: '{}'", input);
        return InputType.INVALID;
    }

    /**
     * Проверяет, является ли строка URL
     *
     * @param input строка для проверки
     * @return true, если строка является URL, иначе false
     */
    private static boolean isValidUrl(String input) {
        try {
            URL url = new URI(input).toURL();
            String protocol = url.getProtocol();
            return HTTP.equals(protocol) || HTTPS.equals(protocol);
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Проверяет, является ли строка путем (GLOB выражением)
     *
     * @param input строка для проверки
     * @return true, если строка является путем, иначе false
     */
    private static boolean isValidPath(String input) {
        try {
            FileSystems.getDefault().getPathMatcher("glob:" + input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
