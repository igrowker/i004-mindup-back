package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

public record RequestCreateAppointmentDto(
    String patientId,
    String psychologistId,
    LocalDateTime date
) { }
