package com.gym.management.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponseDTO {

    private String accessToken;   // short-lived
    private String refreshToken;  // long-lived
    private String username;
    private String role;
}