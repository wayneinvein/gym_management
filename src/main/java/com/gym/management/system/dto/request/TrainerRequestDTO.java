package com.gym.management.system.dto.request;

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
    private String trainerName;

    // Gender of the trainer
    private String trainerGender;

    // Contact number of the trainer
    private String phoneNumber;
}