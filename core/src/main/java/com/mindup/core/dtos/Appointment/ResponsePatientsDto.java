package com.mindup.core.dtos.Appointment;

import lombok.Builder;

@Builder
public record ResponsePatientsDto(
    String userId,
    String name,
    String email

) { }
