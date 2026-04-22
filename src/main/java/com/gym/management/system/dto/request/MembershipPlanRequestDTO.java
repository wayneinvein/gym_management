package com.gym.management.system.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO for creating or updating a membership plan.
 *
 * The active field is intentionally excluded — plans are always
 * active on creation and toggled separately via PATCH /status endpoint.
 */
@Data
public class MembershipPlanRequestDTO {

    // Name of the plan — must be unique
    @NotBlank(message = "Plan name is required")
    @Size(min = 2, max = 100, message = "Plan name must be between 2 and 100 characters")
    private String name;

    // Optional description of the plan
    private String description;

    // Duration in days — must be at least 1 day
    @Min(value = 1, message = "Duration must be at least 1 day")
    private int durationDays;

    // Price must be greater than 0
    @Positive(message = "Price must be a positive value")
    private double price;
}