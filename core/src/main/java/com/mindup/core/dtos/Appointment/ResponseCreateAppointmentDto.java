package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

import com.mindup.core.dtos.User.UserDTO;
import com.mindup.core.enums.AppointmentStatus;

import lombok.Builder;

@Builder
public record ResponseCreateAppointmentDto( 
    String id,
    UserDTO patient,
    UserDTO psychologist,
    LocalDateTime date,
    AppointmentStatus status
    ) {

}
