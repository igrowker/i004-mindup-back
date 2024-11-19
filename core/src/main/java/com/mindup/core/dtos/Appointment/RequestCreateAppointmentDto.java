package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record RequestCreateAppointmentDto(
<<<<<<< Updated upstream
    Long patientId,
    Long psychologistId,
=======
    @NotNull(message ="The Patient ID is required")
    String patientId,

    @NotNull(message ="The Psychologist ID is required")
    String psychologistId,
    
    @NotNull(message = "The date of the Appointment is required")
    @Future(message = "The date of the Appointmest must be on future")
>>>>>>> Stashed changes
    LocalDateTime date
) { }
