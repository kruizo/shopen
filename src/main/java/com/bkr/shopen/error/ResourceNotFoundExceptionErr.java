package com.bkr.shopen.error;

public class ResourceNotFoundExceptionErr extends RuntimeException {
    public ResourceNotFoundExceptionErr(String message) { super(message); }
    public ResourceNotFoundExceptionErr(String message, Throwable cause) { super(message, cause); }
    public ResourceNotFoundExceptionErr(Throwable cause) { super(cause); }
}
