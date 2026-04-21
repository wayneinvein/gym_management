package com.gym.management.system.service.interfaces;

import com.gym.management.system.entity.RefreshToken;
import com.gym.management.system.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    void deleteByUser(User user);
}
