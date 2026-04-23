package com.gym.management.system.dto.request;

import com.gym.management.system.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for recording a member payment.
 *
 * Admin provides member ID, membership ID, amount,
 * payment date, and method. Status defaults to PAID on creation.
 */
@Data
public class MemberPaymentRequestDTO {

    // Member making the payment
    @NotNull(message = "Member ID is required")
    private Long memberId;

    // Membership this payment is for
    @NotNull(message = "Membership ID is required")
    private Long membershipId;

    // Amount paid — must be positive
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    // Date payment was received
    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    // How the payment was made
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    // Optional notes (transaction ID, receipt number etc)
    private String notes;
}