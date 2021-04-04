package com.medical.taskdemo.exception;


public class FileEmptyException extends RuntimeException {
    public FileEmptyException(String message) {
        super(message);
    }

    public FileEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
