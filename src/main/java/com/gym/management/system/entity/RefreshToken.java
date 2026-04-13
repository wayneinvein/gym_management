package com.gym.management.system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Entity representing a refresh token.
 * Used to generate new access tokens without re-authentication.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    // Primary key (auto-generated)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique refresh token string
    @Column(unique = true, nullable = false)
    private String token;

    // Username associated with this token
    private String username;

    // Expiry timestamp of the refresh token
    private Instant expiryDate;
}