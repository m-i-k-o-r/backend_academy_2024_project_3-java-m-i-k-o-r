package backend.academy.sourse;

import backend.academy.source.discovery.InputTypeDetector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputTypeDetectorTest {
    @Test
    void testIdentifyValidUrl() {
        assertEquals(InputTypeDetector.InputType.URL, InputTypeDetector.identify("http://example.com"));

        assertEquals(InputTypeDetector.InputType.URL, InputTypeDetector.identify("https://example.com"));
    }

    @Test
    void testIdentifyValidPath() {
        assertEquals(InputTypeDetector.InputType.PATH, InputTypeDetector.identify("src/main/java"));

        assertEquals(InputTypeDetector.InputType.PATH, InputTypeDetector.identify("**.txt"));
    }
}
