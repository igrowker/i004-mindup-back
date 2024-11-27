package com.mindup.core.dtos.User;

public record ResponseLoginDto(
        String userId,
        String email,
        String token
) {
}
