package com.gym.management.system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for JWT operations.
 *
 * Responsibilities:
 * 1. Generate JWT tokens
 * 2. Extract data (claims) from token
 * 3. Validate token integrity and expiry
 */
@Component
public class JwtUtil {

    /**
     * Secret key used for signing JWT tokens.
     * MUST be kept secure in production (use env variables or vault).
     */
    private final Key SECRET_KEY =
            Keys.hmacShaKeyFor("sandeep_secret_key_sandeep_secret_key".getBytes());

    /**
     * Token validity duration (10 hours)
     */
    private final long EXPIRATION_TIME = 1000 * 60 * 15;

    /**
     * Generates a JWT token for authenticated user.
     *
     * @param username unique identifier of user
     * @param role user role (ADMIN, USER, etc.)
     * @return signed JWT token
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username) // stores username as principal identity
                .claim("role", role)  // custom claim for authorization
                .setIssuedAt(new Date()) // token creation time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // expiry
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // sign using HMAC SHA-256
                .compact();
    }

    /**
     * Extracts username (subject) from JWT token.
     *
     * @param token JWT token
     * @return username stored in token
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Validates token:
     * 1. Username matches
     * 2. Token is not expired
     */
    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    /**
     * Checks whether token is expired.
     */
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    /**
     * Parses JWT token and extracts all claims.
     * This is the core method used internally by other methods.
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}