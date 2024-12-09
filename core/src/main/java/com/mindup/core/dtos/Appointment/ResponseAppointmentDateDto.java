package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

import com.mindup.core.enums.AppointmentStatus;

public record ResponseAppointmentDateDto(
    String id,
    String psychologistId,
    String psychologistName,
    String patientId,
    String patientName,
    AppointmentStatus status,
    LocalDateTime date

) { }
