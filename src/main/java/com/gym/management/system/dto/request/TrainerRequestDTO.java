package com.gym.management.system.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO for creating or updating a trainer.
 *
 * Carries trainer details from the client to the service layer.
 * Sensitive fields like salary, active status, and joinedDate
 * are included here since admin controls trainer creation fully.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRequestDTO {

    // Full name of the trainer
    @NotBlank(message = "Trainer name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String trainerName;

    // Gender of the trainer
    @NotBlank(message = "Gender is required")
    private String trainerGender;

    // 10-digit Indian phone number — also used as login username
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid Indian phone number. Must be 10 digits starting with 6-9"
    )
    private String phoneNumber;

    // Optional email address
    @Email(message = "Invalid email format")
    private String email;

    // Area of expertise
    private String specialization;

    // Monthly salary of the trainer
    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive value")
    private Double salary;
}