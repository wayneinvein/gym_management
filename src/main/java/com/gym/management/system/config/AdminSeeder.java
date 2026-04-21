package com.gym.management.system.config;

import com.gym.management.system.entity.User;
import com.gym.management.system.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.gym.management.system.enums.UserRoles.ADMIN;

/**
 * Seeds a default ADMIN user into the database at application startup.
 *
 * This ensures that the system always has at least one admin account
 * available for initial access and management.
 */
@Component
public class AdminSeeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @PostConstruct
    public void createAdmin() {
        if (!userRepository.existsByUserRole(ADMIN)) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setUserRole(ADMIN);
            userRepository.save(admin);
        }
    }
}