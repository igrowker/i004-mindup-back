package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

import com.mindup.core.enums.AppointmentStatus;

import lombok.Builder;

@Builder
public record ResponseDeleteAppointmentDto(
    String appointmentId,
    LocalDateTime dateSoftDelete,
    AppointmentStatus status

) { }
