package com.gym.management.system.controller;

import com.gym.management.system.dto.request.AuthRequest;
import com.gym.management.system.dto.response.AuthResponse;
import com.gym.management.system.entity.User;
import com.gym.management.system.repository.UserRepository;
import com.gym.management.system.security.JwtUtil;

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

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getUserRole().name()
        );

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getUserRole().name()
        );
    }
}