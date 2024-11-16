package backend.academy.model;

public record HttpRequest(
    String method,
    String path,
    String protocol
) {

}
