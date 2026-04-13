package com.gym.management.system.dto.response;

import lombok.Data;

/**
 * DTO for returning membership plan details.
 * Exposes plan information to clients in a structured format.
 */
@Data
public class MembershipPlanResponseDTO {

    // Unique identifier of the membership plan
    private Long planId;

    // Name of the plan (e.g., Monthly, Quarterly, Yearly)
    private String name;

    // Duration of the plan in days
    private int durationDays;

    // Price of the plan
    private double price;

    // Indicates whether the plan is currently active
    private boolean active;
}