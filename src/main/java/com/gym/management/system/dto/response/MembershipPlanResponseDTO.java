package com.gym.management.system.dto.response;

import lombok.Data;

/**
 * DTO for returning membership plan details in API responses.
 */
@Data
public class MembershipPlanResponseDTO {

    // Unique identifier of the plan
    private Long planId;

    // Name of the plan
    private String name;

    // Description of the plan
    private String description;

    // Duration in days
    private int durationDays;

    // Price in rupees
    private double price;

    // Whether the plan is currently active
    private boolean active;
}