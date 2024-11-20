package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

import com.mindup.core.enums.AppointmentStatus;

public record ResponseAppointmentDto(
    String appointmenId,
    String patientId,
    String psychologistId,
    LocalDateTime date,
    AppointmentStatus status
) { }
