package com.bkr.shopen.error;

public class EmailNotVerifiedExceptionErr extends RuntimeException {
    public EmailNotVerifiedExceptionErr(String message) {
        super(message);
    }

    public EmailNotVerifiedExceptionErr(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotVerifiedExceptionErr(Throwable cause) {
        super(cause);
    }
    
}
