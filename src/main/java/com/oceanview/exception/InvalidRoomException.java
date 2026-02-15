package com.oceanview.exception;


public class InvalidRoomException extends AppException {

    public InvalidRoomException(String message) {
        super(message);
    }

    public InvalidRoomException(String message, Throwable cause) {
        super(message, cause);
    }
}