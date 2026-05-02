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

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only letters and spaces")
    private String memberName;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female or Other")
    private String memberGender;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian phone number")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}