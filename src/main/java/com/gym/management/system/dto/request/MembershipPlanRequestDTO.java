package com.gym.management.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for creating or updating membership plans.
 * Carries plan details from client to service layer.
 */
@Data
public class MembershipPlanRequestDTO {

    // Name of the membership plan (e.g., Monthly, Yearly)
    @NotBlank(message = "name is required")
    private String name;

    // Duration of the plan in days
    @NotNull(message = "enter days")
    private int durationDays;

    // Price of the membership plan
    @NotNull(message = "enter is required")
    private double price;

    // Indicates whether the plan is active
    @NotBlank(message = "tell whether its active or not")
    private boolean active;
}