package com.mindup.chat.dtos;

import jakarta.validation.constraints.NotNull;

public record RequestMessageDto(
        @NotNull
        String patientId,
        @NotNull
        String sender,
        @NotNull
        String content
) {
}
