package com.mindup.core.exceptions;

public class ProfileImageValidationException extends RuntimeException {

    public ProfileImageValidationException(String message) {
        super(message);
    }

    public ProfileImageValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}