package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

public record RequestUpdateAppointmentDto(
    String appointmenId,
    String patientId,
    String psychologistId,
    LocalDateTime date
) { }
