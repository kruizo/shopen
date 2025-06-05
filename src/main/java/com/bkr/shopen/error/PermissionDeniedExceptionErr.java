package com.bkr.shopen.error;

public class PermissionDeniedExceptionErr extends RuntimeException {
    public PermissionDeniedExceptionErr(String message) { super(message); }
    public PermissionDeniedExceptionErr(String message, Throwable cause) { super(message, cause); }
    public PermissionDeniedExceptionErr(Throwable cause) { super(cause); }
}