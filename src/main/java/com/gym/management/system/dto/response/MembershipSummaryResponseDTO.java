package com.gym.management.system.dto.response;

import com.gym.management.system.enums.MembershipStatus;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for returning membership summary for a logged-in member.
 *
 * Provides a complete picture of the member's current membership
 * including days completed, days remaining, and next payment due.
 */
@Data
public class MembershipSummaryResponseDTO {

    // Plan details
    private String planName;
    private String planDescription;
    private double planPrice;

    // Membership dates
    private LocalDate startDate;
    private LocalDate endDate;

    // Current status
    private MembershipStatus status;

    // Day calculations
    private long totalDays;         // total duration of plan in days
    private long daysCompleted;     // days since start date
    private long daysRemaining;     // days until end date

    // Payment info
    private double amountDue;       // amount pending if payment not done
    private LocalDate nextPaymentDue; // end date = next renewal date
    private boolean paymentPending;   // true if payment status is PENDING
}