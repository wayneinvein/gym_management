package com.gym.management.system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
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
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Token validity duration
     */
    @Value("${jwt.expiration}")
    private long expirationTime;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * Generates a JWT token for authenticated user.
     *
     * @param username unique identifier of user
     * @param role user role (ADMIN, USER, etc.)
     * @return signed JWT token
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
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
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}