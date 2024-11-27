package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

import com.mindup.core.enums.AppointmentStatus;

import lombok.Builder;

@Builder
public record ResponseReactivateAppointmentDto( 
String appointmenId,
String patientId,
String psychologistId,
LocalDateTime date,
LocalDateTime softDelete,
AppointmentStatus status
) { }
