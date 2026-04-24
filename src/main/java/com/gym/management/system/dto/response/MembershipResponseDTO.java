package com.gym.management.system.dto.response;

import com.gym.management.system.enums.MembershipStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning membership details in API responses.
 *
 * Uses flat fields instead of full entity objects to keep
 * the response clean and avoid circular reference issues.
 */
@Data
public class MembershipResponseDTO {

    // Unique identifier of the membership
    private Long membershipId;

    // Start date of the membership
    private LocalDate startDate;

    // End date of the membership
    private LocalDate endDate;

    // Current status of the membership
    private MembershipStatus status;

    // Member details — flat fields instead of full Member object
    private Long memberId;
    private String memberName;

    // Plan details — flat fields instead of full MembershipPlan object
    private Long planId;
    private String planName;

    // When this membership record was created
    private LocalDateTime createdAt;
}