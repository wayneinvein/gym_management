package com.gym.management.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO returned after successful authentication.
 * Contains JWT access token and refresh token.
 */
@Data
@AllArgsConstructor
public class AuthResponseDTO {

    // Short-lived JWT used for accessing secured endpoints
    private String accessToken;

    // Long-lived token used to generate new access tokens
    private String refreshToken;
}