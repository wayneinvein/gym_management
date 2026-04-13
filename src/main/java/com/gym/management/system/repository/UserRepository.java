package com.gym.management.system.repository;

import com.gym.management.system.entity.User;
import com.gym.management.system.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for User entity.
 * Provides database operations for authentication and user management.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Fetch user by username for authentication
    Optional<User> findByUsername(String username);

    // Check if any user exists with a specific role
    boolean existsByUserRole(UserRoles role);
}