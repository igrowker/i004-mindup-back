package com.mindup.core.utils;

import com.mindup.core.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidationUtil {

    private final JwtService jwtService;

    public void validateUserId(HttpServletRequest request, String urlUserId, String expectedRole) {
        String token = request.getHeader("Authorization").substring(7);

        String tokenUserId = jwtService.extractUserId(token);
        String tokenRole = jwtService.extractRole(token);

        if (tokenUserId == null || tokenUserId.isEmpty() || !tokenUserId.equals(urlUserId)) {
            throw new SecurityException("Unauthorized Access: User ID does not match.");
        }

        if (expectedRole != null && (tokenRole == null || !tokenRole.equals(expectedRole))) {
            throw new SecurityException("Unauthorized Access: Role mismatch.");
        }
    }
}
