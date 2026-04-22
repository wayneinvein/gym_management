package com.gym.management.system.dto.response;

import com.gym.management.system.enums.MemberStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning member details in API responses.
 *
 * Only exposes safe and relevant fields to the client.
 * Sensitive fields like password, user_id are never included.
 * Trainer name is included as a simple string instead of the
 * full Trainer object to keep the response clean and flat.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {

    // Unique identifier of the member
    private Long memberId;

    // Full name of the member
    private String memberName;

    // Gender of the member
    private String memberGender;

    // Contact number of the member
    private String phoneNumber;

    // Email address of the member
    private String email;

    // Physical address of the member
    private String address;

    // Date of birth of the member
    private LocalDate dateOfBirth;

    // Current membership status (ACTIVE, INACTIVE, SUSPENDED)
    private MemberStatus status;

    // Date when the member first joined the gym
    private LocalDate joinedDate;

    // Name of the assigned trainer — just the name, not the full trainer object
    // null if no trainer is assigned
    private String trainerName;

    // Record creation timestamp
    private LocalDateTime createdAt;

    // Record last updated timestamp
    private LocalDateTime updatedAt;
}