package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;

@Builder
public record RequestUpdateAppointmentDto(
    @NotNull(message = "El id de la cita es obligatorio")
    String id,
    @NotNull(message = "La fecha es obligatoria")
    @Future
    LocalDateTime date
) { }
    