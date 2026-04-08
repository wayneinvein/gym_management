package com.gym.management.system.service.interfaces;

import com.gym.management.system.entity.RefreshToken;

public interface RefreshTokenService {

    public RefreshToken createRefreshToken(String username);
    public RefreshToken verifyExpiration(RefreshToken token);
}
