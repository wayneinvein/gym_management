package com.gym.management.system.controller;

import com.gym.management.system.dto.request.AuthRequestDTO;
import com.gym.management.system.dto.response.AuthResponseDTO;
import com.gym.management.system.service.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles authentication (login + refresh)
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Login user and return tokens
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Generate new access token using refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestBody String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    /**
     * Logout user (invalidate refresh token)
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String token) {
        authService.logout(token);
        return ResponseEntity.ok("Logged out successfully");
    }
}