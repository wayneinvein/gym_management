package com.gym.management.system.dto.response;

import com.gym.management.system.enums.UserRoles;
import lombok.Data;

/**
 * DTO for returning user details.
 * Exposes non-sensitive user information to clients.
 */
@Data
public class UserResponseDTO {

    // Unique identifier of the user
    private Long id;

    // Username used for login
    private String username;

    // Role assigned to the user (e.g., ADMIN, TRAINER, RECEPTIONIST)
    private UserRoles userRole;
}