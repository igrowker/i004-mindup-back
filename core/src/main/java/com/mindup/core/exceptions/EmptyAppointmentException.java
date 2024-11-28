package com.mindup.core.exceptions;

public class EmptyAppointmentException extends RuntimeException {
    public EmptyAppointmentException(String message) {
        super(message);
    }
}
