package com.mindup.chat.dtos;

import jakarta.validation.constraints.NotNull;

public record RequestMessageDto(
        @NotNull(message = "El id del patient no puede ser nulo")
        String patientId,
        @NotNull(message = "El sender no puede ser nulo")
        String sender,
        @NotNull(message = "El content no puede ser nulo")
        String content
) {
}
