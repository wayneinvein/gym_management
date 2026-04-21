package com.gym.management.system.dto.request;

import com.gym.management.system.enums.UserRoles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for creating a new user.
 * Contains credentials and role information.
 */
@Data
public class UserRequestDTO {

    // Unique username for login
    @NotBlank(message = "username is required")
    private String username;

    // Password for authentication
    @NotBlank(message = "password is required")
    private String password;

    // Role assigned to the user (e.g., ADMIN, TRAINER, RECEPTIONIST)
    @NotNull
    private UserRoles userRole;
}