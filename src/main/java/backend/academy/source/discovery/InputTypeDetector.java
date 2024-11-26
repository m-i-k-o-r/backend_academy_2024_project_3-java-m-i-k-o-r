package backend.academy.source.discovery;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static backend.academy.utils.Constants.HTTP;
import static backend.academy.utils.Constants.HTTPS;

@UtilityClass
public class InputTypeDetector {
    public enum InputType {
        URL,
        PATH,
        INVALID
    }

    private static final Logger LOGGER = LogManager.getLogger(InputTypeDetector.class);

    private static final Pattern PATH_PATTERN = Pattern.compile("^[A-Za-z]:/|^\\./|^[*?\\[\\]]");

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

    private static boolean isValidUrl(String input) {
        try {
            URL url = new URI(input).toURL();
            String protocol = url.getProtocol();
            return HTTP.equals(protocol) || HTTPS.equals(protocol);
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }

    private static boolean isValidPath(String input) {
        if (!PATH_PATTERN.matcher(input).matches()) {
            return false;
        }
        try {
            FileSystems.getDefault().getPathMatcher("glob:" + input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
