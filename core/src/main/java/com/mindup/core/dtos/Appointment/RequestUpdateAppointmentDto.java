package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import com.mindup.core.enums.AppointmentStatus;

import lombok.Builder;

@Builder
public record RequestUpdateAppointmentDto(
    @NotNull(message = "El id de la cita es obligatorio")
    String appointmenId,
    @NotNull(message = "El id de paciente es obligatorio")
    String patientId,
    @NotNull(message = "El id del psicologo es obligatorio")
    String psychologistId,   
    @NotNull(message = "La fecha es obligatoria")
    @Future
    LocalDateTime date,
    @NotNull(message = "El status a cambiar es obligatorio")
    AppointmentStatus appointmentStatus
) { }
    