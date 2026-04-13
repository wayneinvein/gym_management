package com.gym.management.system.dto.request;

import lombok.Data;

/**
 * DTO for creating or updating membership plans.
 * Carries plan details from client to service layer.
 */
@Data
public class MembershipPlanRequestDTO {

    // Name of the membership plan (e.g., Monthly, Yearly)
    private String name;

    // Duration of the plan in days
    private int durationDays;

    // Price of the membership plan
    private double price;

    // Indicates whether the plan is active (defaults can be handled in backend)
    private boolean active; // optional (can default to true if not sent)
}