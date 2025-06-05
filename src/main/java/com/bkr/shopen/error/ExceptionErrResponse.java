package com.bkr.shopen.error;

import java.time.LocalDateTime;

public class ExceptionErrResponse {
    private final String message;
    private final int status;
    private final String path;
    private final LocalDateTime timestamp;

    public ExceptionErrResponse(String message, int status, String path, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = timestamp;
        
    }

    public String getMessage() { return message; }
    public int getStatus() { return status; }
    public String getPath() { return path; }
    public LocalDateTime getTimestamp() { return timestamp; }
}