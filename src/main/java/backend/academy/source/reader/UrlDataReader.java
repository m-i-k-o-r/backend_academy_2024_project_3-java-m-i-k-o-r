package backend.academy.source.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;

/**
 * Реализация {@link DataReader} для чтения данных из URL
 */
@AllArgsConstructor
public class UrlDataReader extends DataReader {
    private final URL url;

    @Override
    protected BufferedReader createReader() throws IOException {
        try {
            HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(url.toURI())
                .GET()
                .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            return new BufferedReader(new InputStreamReader(
                response.body(),
                StandardCharsets.UTF_8
            ));
        } catch (Exception e) {
            throw new IOException("Ошибка при открытии соединения: " + e.getMessage(), e);
        }
    }

    @Override
    public String getSource() {
        return this.url.toString();
    }
}
