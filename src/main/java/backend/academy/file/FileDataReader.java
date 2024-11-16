package backend.academy.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Getter
@AllArgsConstructor
public class FileDataReader extends DataReader {
    private final Path path;

    @Override
    public BufferedReader createReader() throws IOException {
        return new BufferedReader(new FileReader(
                path.toFile(),
                StandardCharsets.UTF_8
        ));
    }

    @Override
    public String getSource() {
        return this.path.toString();
    }
}

