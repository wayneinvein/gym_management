package com.gym.management.system.controller;

import com.gym.management.system.dto.request.AuthRequestDTO;
import com.gym.management.system.dto.request.ChangePasswordRequestDTO;
import com.gym.management.system.dto.request.LogoutRequestDTO;
import com.gym.management.system.dto.request.RefreshRequestDTO;
import com.gym.management.system.dto.response.AuthResponseDTO;
import com.gym.management.system.security.SecurityUtils;
import com.gym.management.system.service.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Handles authentication (login + refresh)
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SecurityUtils securityUtils;

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
    public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody RefreshRequestDTO refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken.getRefreshToken()));
    }

    /**
     * Logout user (invalidate refresh token)
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequestDTO token) {
        authService.logout(token.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Changes password for the currently logged-in user.
     * Accessible by all authenticated users (ADMIN, TRAINER, MEMBER).
     */
    @Operation(summary = "Change password", description = "Changes password for currently logged-in user")
    @PatchMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequestDTO dto) {

        // Get username of currently logged-in user from security context
        String username = securityUtils.getCurrentUsername();
        authService.changePassword(username, dto);
        return ResponseEntity.ok("Password changed successfully");
    }
}