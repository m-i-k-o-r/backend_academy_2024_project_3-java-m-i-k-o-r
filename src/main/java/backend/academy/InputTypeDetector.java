package backend.academy;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class InputTypeDetector {
    public enum InputType {
        URL,
        PATH,
        INVALID
    }

    private static final Pattern PATH_PATTERN = Pattern.compile("^(\\./|[A-Za-z]:/).*([*?\\[\\]]+.*)?$");

    public static InputType identify(String input) {
        if (isValidUrl(input)) {
            return InputType.URL;
        }
        if (isValidPath(input)) {
            return InputType.PATH;
        }
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
