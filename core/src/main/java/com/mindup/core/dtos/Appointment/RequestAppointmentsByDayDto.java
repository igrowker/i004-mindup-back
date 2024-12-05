package com.mindup.core.dtos.Appointment;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record RequestAppointmentsByDayDto( 
    @NotNull
    String psychologistId,
    @NotNull
    LocalDate date
) { }
