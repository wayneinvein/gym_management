package com.gym.management.system.dto.response;

import lombok.*;

/**
 * DTO for returning trainer details in API responses.
 * Exposes basic trainer information to clients.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponseDTO {

    // Unique identifier of the trainer
    private Long trainerId;

    // Name of the trainer
    private String trainerName;

    // Gender of the trainer
    private String trainerGender;

    // Contact number of the trainer
    private String phoneNumber;
}