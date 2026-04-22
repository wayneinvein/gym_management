package com.gym.management.system.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning trainer details in API responses.
 *
 * Only exposes safe and relevant fields.
 * Sensitive fields like password and user_id are never included.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponseDTO {

    // Unique identifier of the trainer
    private Long trainerId;

    // Full name of the trainer
    private String trainerName;

    // Gender of the trainer
    private String trainerGender;

    // Contact number of the trainer
    private String phoneNumber;

    // Email address of the trainer
    private String email;

    // Area of expertise
    private String specialization;

    // Date when trainer joined the gym
    private LocalDate joiningDate;

    // Monthly salary
    private double salary;

    // Whether trainer is currently active
    private boolean active;

    // Record creation timestamp
    private LocalDateTime createdAt;

    // Record last updated timestamp
    private LocalDateTime updatedAt;
}