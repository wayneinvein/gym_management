package com.gym.management.system.dto.response;

import com.gym.management.system.entity.Member;
import com.gym.management.system.entity.MembershipPlan;
import com.gym.management.system.enums.MembershipStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO for returning membership details.
 * Includes membership info along with associated member and plan.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipResponseDTO {

    // Unique identifier of the membership
    private Long membershipId;

    // Start date of the membership
    private LocalDate startDate;

    // End date calculated based on plan duration
    private LocalDate endDate;

    // Current status of membership (e.g., ACTIVE, EXPIRED)
    private MembershipStatus status;

    // Associated member details
    private Member member;

    // Associated membership plan details
    private MembershipPlan plan;

}