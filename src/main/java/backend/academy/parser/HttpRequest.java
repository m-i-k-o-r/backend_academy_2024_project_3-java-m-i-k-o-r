package backend.academy.parser;

public record HttpRequest(
    String method,
    String path,
    String protocol
) {

}
