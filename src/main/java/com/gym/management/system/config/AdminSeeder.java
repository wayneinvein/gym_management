package com.gym.management.system.config;

import com.gym.management.system.entity.User;
import com.gym.management.system.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Executes automatically after the bean is initialized.
     *
     * Logic:
     * - Checks if any ADMIN user already exists
     * - If not, creates a default admin account
     *
     * This prevents duplicate admin creation on every restart.
     */
    @PostConstruct
    public void createAdmin() {

        // Avoid creating multiple admin users if one already exists
        if (!userRepository.existsByUserRole(ADMIN)) {

            // Create default admin credentials (should be changed in production)
            User admin = new User();
            admin.setUsername("admin");

            // Always store passwords in encoded (hashed) form
            admin.setPassword(passwordEncoder.encode("admin123"));

            admin.setUserRole(ADMIN);

            // Persist the admin user to the database
            userRepository.save(admin);
        }
    }
}