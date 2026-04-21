package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.request.AuthRequestDTO;
import com.gym.management.system.dto.response.AuthResponseDTO;
import com.gym.management.system.entity.RefreshToken;
import com.gym.management.system.entity.User;
import com.gym.management.system.repository.RefreshTokenRepository;
import com.gym.management.system.repository.UserRepository;
import com.gym.management.system.security.JwtUtil;
import com.gym.management.system.service.interfaces.AuthService;
import com.gym.management.system.service.interfaces.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Service responsible for authentication workflow.
 *
 * Handles user login, token refresh, and logout operations.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Authenticates user credentials and generates access + refresh tokens.
     */
    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {

        // Authenticate username and password using Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Fetch user details after successful authentication
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT access token
        String accessToken = jwtUtil.generateToken(
                user.getUsername(),
                user.getUserRole().name()
        );

        // deletes old refresh token
        refreshTokenRepository.deleteByUsername(user.getUsername());

        // Generate refresh token and store it in DB
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user.getUsername());

        // Return both tokens to client
        return new AuthResponseDTO(
                accessToken,
                refreshToken.getToken()
        );
    }

    /**
     * Generates a new access token using a valid refresh token.
     */
    @Override
    public AuthResponseDTO refreshToken(String requestToken) {

        // Validate and fetch existing refresh token
        RefreshToken oldToken = refreshTokenService.verifyRefreshToken(requestToken);

        // Fetch user
        User user = userRepository.findByUsername(oldToken.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete old refresh token
        refreshTokenRepository.delete(oldToken);

        // Issue new refresh token
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        // Generate new access token
        String accessToken = jwtUtil.generateToken(
                user.getUsername(),
                user.getUserRole().name()
        );

        // Return new access token + new refresh token
        return new AuthResponseDTO(accessToken, newRefreshToken.getToken());
    }

    /**
     * Logs out user by deleting refresh token from database.
     */
    @Override
    public void logout(String token) {
            refreshTokenRepository.deleteByToken(token);

        }
    }