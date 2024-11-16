package backend.academy.source.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Getter
@AllArgsConstructor
public class UrlDataReader extends DataReader {
    private final URL url;

    @Override
    public BufferedReader createReader() throws IOException {
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
