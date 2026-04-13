package com.gym.management.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for creating or updating a gym member.
 * Includes basic validation for required fields.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDTO {

    // Member's full name (must not be empty)
    @NotBlank(message = "Name cannot be blank")
    private String memberName;

    // Member's gender (required field)
    @NotBlank(message = "Gender is required")
    private String memberGender;

    // Member's phone number (must be a valid 10-digit Indian number starting from 6-9)
    @NotNull(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid Indian phone number"
    )
    private String phoneNumber;
}