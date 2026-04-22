package com.gym.management.system.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO for creating or updating a gym member.
 *
 * Carries member details from the client to the service layer.
 * Validation annotations ensure data integrity before processing.
 * Sensitive fields like status and joinedDate are NOT included here
 * — those are set automatically by the service, not by the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDTO {

    // Full name of the member — must not be blank
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String memberName;

    // Gender of the member — must not be blank
    @NotBlank(message = "Gender is required")
    private String memberGender;

    // 10-digit Indian phone number starting with 6-9
    // Used as login username when member account is auto-created
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid Indian phone number. Must be 10 digits starting with 6-9"
    )
    private String phoneNumber;

    // Optional email address
    @Email(message = "Invalid email format")
    private String email;

    // Optional address
    private String address;

    // Optional date of birth — must be a past date if provided
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}