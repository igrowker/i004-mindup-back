package com.mindup.core.security;

import com.mindup.core.exceptions.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String email, String userId, String name, String image, String role) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", userId);
        extraClaims.put("name", name);
        extraClaims.put("image", image);
        extraClaims.put("role", role);
        return buildToken(extraClaims, email, jwtExpiration);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.getOrDefault("role", "UNKNOWN").toString());
    }

    public String extractUserId(String token) {
        String userId = extractClaim(token, claims -> claims.getOrDefault("userId", "").toString());
        if (userId == null || userId.isEmpty()) {
            throw new SecurityException("Invalid token: does not contain a user ID.");
        }
        return userId;
    }

    public boolean isTokenValid(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }

    public boolean isUserIdValid(String token, String urlUserId) {
        String tokenUserId = extractUserId(token);
        return tokenUserId.equals(urlUserId);
    }

    public boolean isRoleValid(String token, String[] validRoles) {
        String tokenRole = extractRole(token);
        for (String role : validRoles) {
            if (role.equals(tokenRole)) {
                return true;
            }
        }
        return false;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            String email,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new TokenExpiredException("Token has expired");
        }
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }
}
