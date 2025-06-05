package com.bkr.shopen.error;

public class InternalServerExceptionErr extends RuntimeException {
    public InternalServerExceptionErr(String message) { super(message); }
    public InternalServerExceptionErr(String message, Throwable cause) { super(message, cause); }
    public InternalServerExceptionErr(Throwable cause) { super(cause); }
}