package com.oceanview.exception;


public abstract class AppException extends RuntimeException {

    private int errorCode; // optional error code for identifying the error

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getErrorCode() { return errorCode; }
}