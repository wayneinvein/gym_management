package com.gym.management.system.service.interfaces;

import com.gym.management.system.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String username);

    RefreshToken verifyRefreshToken(String token);

    void deleteByToken(String token);
}
