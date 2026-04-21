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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtUtil.generateToken(
                user.getUsername(),
                user.getUserRole().name()
        );

        // Now passing User object instead of username string
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponseDTO(accessToken, refreshToken.getToken());
    }

    @Override
    public AuthResponseDTO refreshToken(String requestToken) {
        RefreshToken oldToken = refreshTokenService.verifyRefreshToken(requestToken);

        // Get user from refresh token directly
        User user = oldToken.getUser();

        refreshTokenRepository.delete(oldToken);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        String accessToken = jwtUtil.generateToken(
                user.getUsername(),
                user.getUserRole().name()
        );

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