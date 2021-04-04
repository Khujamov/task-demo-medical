package com.medical.taskdemo.exception;

public class ContentTypeNotMatchException extends RuntimeException {
    public ContentTypeNotMatchException(String message) {
        super(message);
    }

    public ContentTypeNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
