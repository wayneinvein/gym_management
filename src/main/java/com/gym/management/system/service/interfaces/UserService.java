package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.UserRequestDTO;
import com.gym.management.system.dto.response.UserResponseDTO;

import java.util.List;

/**
 * Service interface for managing users.
 *
 * Provides basic CRUD operations for application users.
 */
public interface UserService {

    // Create a new user
    UserResponseDTO addUser(UserRequestDTO user);

    // Update existing user details
    UserResponseDTO updateUser(UserRequestDTO user, Long id);

    // Delete user by ID
    UserResponseDTO deleteUser(Long id);

    // Fetch all users
    List<UserResponseDTO> getAllUsers();

    // Fetch user by ID
    UserResponseDTO getUserById(Long id);
}