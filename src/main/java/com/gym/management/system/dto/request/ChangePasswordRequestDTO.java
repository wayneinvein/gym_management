package com.gym.management.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for changing a user's password.
 *
 * Requires current password for verification
 * and new password with confirmation to prevent typos.
 */
@Data
public class ChangePasswordRequestDTO {

    // Current password for verification — prevents unauthorized changes
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    // New password to set
    @NotBlank(message = "New password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String newPassword;

    // Confirmation — must match newPassword
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}