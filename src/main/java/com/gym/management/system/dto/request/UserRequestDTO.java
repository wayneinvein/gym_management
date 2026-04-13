package com.gym.management.system.dto.request;

import com.gym.management.system.enums.UserRoles;
import lombok.Data;

/**
 * DTO for creating a new user.
 * Contains credentials and role information.
 */
@Data
public class UserRequestDTO {

    // Unique username for login
    private String username;

    // Password for authentication (should be stored encoded)
    private String password;

    // Role assigned to the user (e.g., ADMIN, TRAINER, RECEPTIONIST)
    private UserRoles userRole;
}