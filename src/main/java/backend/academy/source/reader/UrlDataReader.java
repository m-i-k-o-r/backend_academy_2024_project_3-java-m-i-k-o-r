package backend.academy.source.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UrlDataReader extends DataReader {
    private final URL url;

    @Override
    protected BufferedReader createReader() throws IOException {
        return new BufferedReader(new InputStreamReader(
            url.openStream(),
            StandardCharsets.UTF_8
        ));
    }

    @Override
    public String getSource() {
        return this.url.toString();
    }
}
