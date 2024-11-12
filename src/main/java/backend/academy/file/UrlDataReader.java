package backend.academy.file;

import lombok.AllArgsConstructor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public class UrlDataReader extends DataReader {
    private final URL url;

    @Override
    protected BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(
            url.openStream(),
            StandardCharsets.UTF_8
        ));
    }
}
