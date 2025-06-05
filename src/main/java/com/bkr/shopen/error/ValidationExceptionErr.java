package com.bkr.shopen.error;

public class ValidationExceptionErr extends RuntimeException {
    public ValidationExceptionErr(String message) { super(message); }
    public ValidationExceptionErr(String message, Throwable cause) { super(message, cause); }
    public ValidationExceptionErr(Throwable cause) { super(cause); }
}