package com.mindup.chat.exceptions;

public record ErrorResponse(
        int statusCode,
        String message
) {
}
