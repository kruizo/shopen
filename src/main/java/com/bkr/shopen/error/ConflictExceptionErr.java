package com.bkr.shopen.error;

public class ConflictExceptionErr extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConflictExceptionErr(String message) {
        super(message);
    }

    public ConflictExceptionErr(String message, Throwable cause) {
        super(message, cause);
    }

    public ConflictExceptionErr(Throwable cause) {
        super(cause);
    }
    
}
