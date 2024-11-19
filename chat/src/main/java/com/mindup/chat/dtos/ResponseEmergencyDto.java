package com.mindup.chat.dtos;

public record ResponseEmergencyDto(
        String city,
        String institution,
        String address,
        String telephone,
        String sm,
        String emergency
) {
}
