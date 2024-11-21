package com.mindup.core.enums;

public enum AppointmentStatus {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    CANCELED("CANCELED");

    private final String status;

    AppointmentStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

}
