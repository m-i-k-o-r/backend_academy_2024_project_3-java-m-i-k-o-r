package backend.academy.model;

import java.time.LocalDateTime;

public record LogRecord(
    String remoteAddr,
    String remoteUser,
    LocalDateTime timeLocal,
    HttpRequest request,
    int status,
    int bodyBytesSent,
    String httpReferer,
    String httpUserAgent
) {

}
