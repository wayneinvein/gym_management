package com.gym.management.system.repository;

import com.gym.management.system.entity.User;
import com.gym.management.system.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUserRole(UserRoles role);
}
