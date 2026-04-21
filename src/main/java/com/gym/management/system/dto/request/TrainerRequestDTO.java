package com.gym.management.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO for creating or updating trainer details.
 * Carries basic trainer information from client to backend.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRequestDTO {

    // Name of the trainer
    @NotBlank(message = "name is required")
    private String trainerName;

    // Gender of the trainer
    @NotBlank(message = "gender is required")
    private String trainerGender;

    // Contact number of the trainer
    @NotBlank(message = "number is required")
    private String phoneNumber;
}