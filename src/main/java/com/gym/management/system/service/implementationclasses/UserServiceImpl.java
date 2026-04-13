package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.UserDTOMapper;
import com.gym.management.system.dto.request.UserRequestDTO;
import com.gym.management.system.dto.response.UserResponseDTO;
import com.gym.management.system.entity.User;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.UserRepository;
import com.gym.management.system.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gym.management.system.enums.UserRoles.ADMIN;

/**
 * Service implementation for managing users.
 *
 * Handles user creation, update, deletion, and retrieval.
 * Also enforces business rules like single ADMIN restriction.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;

    @Override
    public UserResponseDTO addUser(UserRequestDTO user) {

        // Ensure only one ADMIN exists in system
        if (user.getUserRole().equals(ADMIN) && userRepository.existsByUserRole(ADMIN)) {
            throw new RuntimeException("Admin already exists. Only one admin allowed.");
        }

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Convert DTO to entity and save
        User newUser = userDTOMapper.toEntity(user);

        return userDTOMapper.toResponse(userRepository.save(newUser));
    }

    @Override
    public UserResponseDTO updateUser(UserRequestDTO user, Long id) {

        // Fetch existing user
        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("user with id: " + id + " not found"));

        // Prevent modification of ADMIN account
        if (existingUser.getUserRole().equals(ADMIN)) {
            throw new RuntimeException("Admin cannot be modified");
        }

        // Prevent creating second ADMIN
        if (user.getUserRole().equals(ADMIN) && userRepository.existsByUserRole(ADMIN)) {
            throw new RuntimeException("Admin already exists. Only one admin allowed.");
        }

        // Update fields
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setUserRole(user.getUserRole());

        return userDTOMapper.toResponse(userRepository.save(existingUser));
    }

    @Override
    public UserResponseDTO deleteUser(Long id) {

        // Fetch user or throw exception
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("user with id: " + id + " not found"));

        // Prevent deletion of ADMIN account
        if (user.getUserRole().equals(ADMIN)) {
            throw new RuntimeException("Admin cannot be deleted");
        }

        userRepository.delete(user);

        return userDTOMapper.toResponse(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        // Fetch and convert all users
        return userDTOMapper.toResponse(userRepository.findAll());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        // Fetch user by id or throw exception
        return userRepository.findById(id)
                .map(userDTOMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }
}