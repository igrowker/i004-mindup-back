package com.mindup.core.dtos.User;

public record ResponseLoginDto(
        String userId,
        String email,
        String name,
        String image,
        String role,
        String token) {

}
