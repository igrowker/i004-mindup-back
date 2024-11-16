package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

public record RequestUpdateAppointmentDto(
    Long appointmenId,
    Long patientId,
    Long psychologistId,
    LocalDateTime date
) { }
