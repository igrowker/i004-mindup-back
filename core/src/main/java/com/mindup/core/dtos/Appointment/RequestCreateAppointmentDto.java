package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RequestCreateAppointmentDto(
    @NotNull(message = "El id de paciente es obligatorio")
    String patientId,
    @NotNull(message = "El id de psycologo es obligatorio")
    String psychologistId,
    
    @NotNull(message = "The date of the Appointment is required")
    @Future(message = "The date of the Appointmest must be on future")
    LocalDateTime date
) { }
