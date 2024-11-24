package backend.academy.source.discovery;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputTypeDetector {
    public enum InputType {
        URL,
        PATH,
        INVALID
    }

    private static final Logger logger = LogManager.getLogger(InputTypeDetector.class);

    private static final Pattern PATH_PATTERN = Pattern.compile("^(\\./|[A-Za-z]:/).*([*?\\[\\]]+.*)?$");

    public static InputType identify(String input) {
        if (isValidUrl(input)) {
            logger.info("Ввод распознан как URL: {}", input);
            return InputType.URL;
        }
        if (isValidPath(input)) {
            logger.info("Ввод распознан как PATH: {}", input);
            return InputType.PATH;
        }
        logger.warn("Ввод не распознан как PATH или URL: {}", input);
        return InputType.INVALID;
    }

    private static boolean isValidUrl(String input) {
        try {
            URL url = new URI(input).toURL();
            String protocol = url.getProtocol();
            return protocol != null && (protocol.equals("http") || protocol.equals("https"));
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }

    private static boolean isValidPath(String input) {
        return PATH_PATTERN.matcher(input).matches();
    }
}
