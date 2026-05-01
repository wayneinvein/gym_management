package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.AuthRequestDTO;
import com.gym.management.system.dto.request.ChangePasswordRequestDTO;
import com.gym.management.system.dto.response.AuthResponseDTO;

/**
 * Service interface for authentication operations.
 *
 * Defines contract for login, refresh token, and logout functionality.
 */
public interface AuthService {

    /**
     * Authenticates user and returns access + refresh tokens.
     */
    AuthResponseDTO login(AuthRequestDTO request);

    /**
     * Generates a new access token using a valid refresh token.
     */
    AuthResponseDTO refreshToken(String refreshToken);

    /**
     * Logs out user by invalidating refresh token.
     */
    void logout(String refreshToken);

    // Change password for currently logged-in user
    void changePassword(String username, ChangePasswordRequestDTO dto);
}