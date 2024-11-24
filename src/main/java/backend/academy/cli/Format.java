package backend.academy.cli;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Format {
    MARKDOWN(".md"),
    ADOC(".adoc");

    private final String label;

    Format(String label) {
        this.label = label;
    }

    public static String getAvailableFormats() {
        return Arrays.stream(values())
            .map(Format::name)
            .reduce((a, b) -> a + ", " + b)
            .orElse("");
    }
}
