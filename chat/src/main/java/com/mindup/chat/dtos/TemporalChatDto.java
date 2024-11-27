package com.mindup.chat.dtos;

import jakarta.validation.constraints.NotNull;


public record TemporalChatDto(
         @NotNull(message = "El id del chat no puede ser nulo")
         String temporalChatId,
         @NotNull(message = "El id del profesional no puede ser nulo")
         String professionalId
) {
}
