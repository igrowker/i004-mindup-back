package com.mindup.core.dtos.User;

public record ResponseLoginDto(
        String id,
        String email,
        String token
) {
}
