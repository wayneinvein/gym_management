package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.entity.RefreshToken;
import com.gym.management.system.repository.RefreshTokenRepository;
import com.gym.management.system.service.interfaces.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final long REFRESH_TOKEN_DURATION = 7; // days

    @Override
    public RefreshToken createRefreshToken(String username) {

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUsername(username);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(
                Instant.now().plus(REFRESH_TOKEN_DURATION, ChronoUnit.DAYS)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }

        return refreshToken;
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
