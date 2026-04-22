package com.gym.management.system.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for creating a new membership subscription.
 *
 * Only startDate is required from the client.
 * endDate is calculated automatically from plan duration.
 * amountPaid is taken from the plan price at time of subscription.
 * status is always set to ACTIVE on creation.
 */
@Data
public class MembershipRequestDTO {

    // Start date of the membership
    // End date will be auto-calculated as startDate + plan.durationDays
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
}