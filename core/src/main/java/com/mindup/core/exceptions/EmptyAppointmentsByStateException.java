package com.mindup.core.exceptions;

public class EmptyAppointmentsByStateException  extends RuntimeException{
    public EmptyAppointmentsByStateException(String message) {
        super(message);
    }
}
