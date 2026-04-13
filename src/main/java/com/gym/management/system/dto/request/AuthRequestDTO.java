package com.gym.management.system.dto.request;

import lombok.Data;

/**
 * DTO for user login request.
 * Carries credentials from client to authentication layer.
 */
@Data
public class AuthRequestDTO {

    // Username of the user attempting to log in
    private String username;

    // Plain text password provided by the user (will be authenticated securely)
    private String password;
}