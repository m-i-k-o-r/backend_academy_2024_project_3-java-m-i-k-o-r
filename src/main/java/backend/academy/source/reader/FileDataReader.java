package backend.academy.source.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FileDataReader extends DataReader {
    private final Path path;

    @Override
    protected BufferedReader createReader() throws IOException {
        return new BufferedReader(new FileReader(
            path.toFile(),
            StandardCharsets.UTF_8
        ));
    }

    @Override
    public String getSource() {
        return this.path.toUri().toString();
    }
}

