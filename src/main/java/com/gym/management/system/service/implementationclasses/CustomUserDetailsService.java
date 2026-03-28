package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.entity.User;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws NotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new NotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getUserRole().name())
                .build();
    }
}