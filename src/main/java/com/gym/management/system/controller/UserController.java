package com.gym.management.system.controller;

import com.gym.management.system.dto.request.UserRequestDTO;
import com.gym.management.system.dto.response.UserResponseDTO;
import com.gym.management.system.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles user management operations.
 * Access: ADMIN only
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserRequestDTO user) {
        return new ResponseEntity<>(
                userService.addUser(user),
                HttpStatus.CREATED
        );
    }

    /**
     * Update existing user.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(
            @RequestBody UserRequestDTO user,
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.updateUser(user, id)
        );
    }

    /**
     * Delete user by ID.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully with id: " + id);
    }

    /**
     * Fetch all users.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Fetch user by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}