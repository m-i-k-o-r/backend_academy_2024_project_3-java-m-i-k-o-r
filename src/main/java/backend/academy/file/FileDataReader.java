package backend.academy.file;

import lombok.AllArgsConstructor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@AllArgsConstructor
public class FileDataReader extends DataReader {
    private final Path path;

    @Override
    protected BufferedReader getReader() throws IOException {
        return new BufferedReader(new FileReader(
            path.toFile(),
            StandardCharsets.UTF_8
        ));
    }
}
