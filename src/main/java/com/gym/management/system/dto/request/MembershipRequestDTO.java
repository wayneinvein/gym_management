package com.gym.management.system.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO for creating a membership record.
 * Captures the start date of the membership.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipRequestDTO {

    // Start date of the membership (must not be null)
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

}