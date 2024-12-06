package com.mindup.core.dtos.Appointment;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ResponsePatientsDto(
    String userId,
    String name,
    String email,
    LocalDateTime nextAppointment,
    String image


) { }
