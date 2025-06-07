package com.bkr.shopen.error;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter 
public class ExceptionErrResponse {
    private final String message;
    private final int status;
    private final String path;
    private final LocalDateTime timestamp;

    public ExceptionErrResponse(int status, String message, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
        
    }

}