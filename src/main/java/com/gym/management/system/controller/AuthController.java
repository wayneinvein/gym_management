package com.gym.management.system.controller;

import com.gym.management.system.dto.request.AuthRequest;
import com.gym.management.system.dto.response.AuthResponse;
import com.gym.management.system.entity.RefreshToken;
import com.gym.management.system.entity.User;
import com.gym.management.system.repository.UserRepository;
import com.gym.management.system.security.JwtUtil;
import com.gym.management.system.service.interfaces.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    //Login (Access + Refresh Token)
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        System.out.println("step 0 done");

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        System.out.println("step 1 done");

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("step 2 done");

        // Access token
        String accessToken = jwtUtil.generateToken(
                user.getUsername(),
                user.getUserRole().name()
        );

        System.out.println("step 3 done");

        // Refresh Token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        System.out.println("step 4 done");

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                user.getUsername(),
                user.getUserRole().name()
        );

    }

    //REFRESH TOKEN (Get new access token)
    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody String refreshToken) {

        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        User user = userRepository.findByUsername(token.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtUtil.generateToken(
                user.getUsername(),
                user.getUserRole().name()
        );

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                user.getUsername(),
                user.getUserRole().name()
        );
    }

    // LOGOUT (Secure version using JWT)
    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader) {

        // Expected: "Bearer eyJhbGciOiJIUzI1NiIs..."
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);

        String username = jwtUtil.extractUsername(token);

        refreshTokenService.deleteByUsername(username);

        return "Logged out successfully";
    }
}