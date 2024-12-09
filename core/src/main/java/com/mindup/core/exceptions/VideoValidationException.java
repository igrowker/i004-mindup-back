package com.mindup.core.exceptions;

public class VideoValidationException extends RuntimeException {

    public VideoValidationException(String message) {
        super(message);
    }

    public VideoValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
