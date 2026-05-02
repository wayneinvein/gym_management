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

@Data
public class TrainerRequestDTO {

    @NotBlank(message = "Trainer name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only letters and spaces")
    private String trainerName;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female or Other")
    private String trainerGender;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian phone number")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 100, message = "Specialization cannot exceed 100 characters")
    private String specialization;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive value")
    private Double salary;
}