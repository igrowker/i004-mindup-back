package com.mindup.core.dtos;

public record ResponseLoginDto(
        Long id,
        String email,
        String token
) {
}
