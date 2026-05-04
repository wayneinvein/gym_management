package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.request.AuthRequestDTO;
import com.gym.management.system.dto.request.ChangePasswordRequestDTO;
import com.gym.management.system.dto.response.AuthResponseDTO;
import com.gym.management.system.entity.RefreshToken;
import com.gym.management.system.entity.User;
import com.gym.management.system.exception.InvalidInputException;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.exception.TokenException;
import com.gym.management.system.repository.RefreshTokenRepository;
import com.gym.management.system.repository.UserRepository;
import com.gym.management.system.security.JwtUtil;
import com.gym.management.system.service.interfaces.AuthService;
import com.gym.management.system.service.interfaces.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for authentication workflow.
 *
 * Handles user login, token refresh, and logout operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates user credentials and generates access + refresh tokens.
     */
    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {

        log.info("Login attempt for username: {}", request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new TokenException("User not found"));

        String accessToken = jwtUtil.generateToken(
                user.getUsername(),
                user.getUserRole().name()
        );

        // Now passing User object instead of username string
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        log.info("tokens generated");

        return new AuthResponseDTO(accessToken, refreshToken.getToken());
    }

    @Override
    public AuthResponseDTO refreshToken(String requestToken) {

        log.info("Refresh token request received");

        //verify refresh token is present
        RefreshToken oldToken = refreshTokenService.verifyRefreshToken(requestToken);

        // Get user from refresh token directly
        User user = oldToken.getUser();

        //delete old token
        refreshTokenRepository.delete(oldToken);

        //create new refresh token
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        //generate new access token
        String accessToken = jwtUtil.generateToken(
                user.getUsername(),
                user.getUserRole().name()
        );

        log.info("new refresh token and its corresponding access token generated");

        return new AuthResponseDTO(accessToken, newRefreshToken.getToken());
    }

    /**
     * Logs out user by deleting refresh token from database.
     */
    @Override
    public void logout(String token) {

        log.info("logout request received");
        refreshTokenRepository.deleteByToken(token);

        }

    /**
     * Changes password for the currently logged-in user.
     *
     * Verifies current password before allowing change.
     * Throws InvalidInputException if current password is wrong
     * or new password and confirm password do not match.
     */
    @Override
    public void changePassword(String username, ChangePasswordRequestDTO dto) {

        log.info("change password request received");

        // Fetch user by username from security context
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        log.info("user fetched from security context");

        // Verify current password matches what is stored in DB
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidInputException("Current password is incorrect");
        }

        log.info("password verified against DB");

        // New password and confirm password must match
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new InvalidInputException("New password and confirm password do not match");
        }

        // New password cannot be same as current password
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new InvalidInputException("New password cannot be same as current password");
        }

        // Encode and save new password
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        log.info("new password encoded and saved in DB");
    }
    }