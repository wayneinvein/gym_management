package com.gym.management.system.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating a membership plan.
 *
 * The active field is intentionally excluded — plans are always
 * active on creation and toggled separately via PATCH /status endpoint.
 */
@Data
public class MembershipPlanRequestDTO {

    @NotBlank(message = "Plan name is required")
    @Size(min = 2, max = 100, message = "Plan name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Plan name must contain only letters, numbers and spaces")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @Min(value = 1, message = "Duration must be at least 1 day")
    @Max(value = 3650, message = "Duration cannot exceed 3650 days (10 years)")
    private int durationDays;

    @Positive(message = "Price must be a positive value")
    @DecimalMax(value = "999999.99", message = "Price cannot exceed 999999.99")
    private double price;
}