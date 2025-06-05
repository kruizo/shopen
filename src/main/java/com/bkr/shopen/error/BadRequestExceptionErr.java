package com.bkr.shopen.error;

public class BadRequestExceptionErr extends RuntimeException {
    public BadRequestExceptionErr(String message) { super(message); }
    public BadRequestExceptionErr(String message, Throwable cause) { super(message, cause); }
    public BadRequestExceptionErr(Throwable cause) { super(cause); }
}