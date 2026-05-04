package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.entity.User;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of UserDetailsService.
 *
 * Used by Spring Security during authentication to load user data
 * from the database and convert it into Spring Security format.
 */
@Service
@Slf4j
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads user by username during authentication process.
     *
     * Converts application User entity into Spring Security UserDetails.
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws NotFoundException {

        log.info("request received for fetching user according to: {}", username);

        // Fetch user from database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new NotFoundException("User not found"));

        log.info("fetched user from database");

        // Convert entity into Spring Security UserDetails object
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getUserRole().name())
                .build();
    }
}