package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

public record RequestCreateAppointmentDto(
    Long patientId,
    Long psychologistId,
    LocalDateTime date
) { }
