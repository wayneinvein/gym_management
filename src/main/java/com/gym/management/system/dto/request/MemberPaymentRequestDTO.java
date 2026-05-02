package com.gym.management.system.dto.request;

import com.gym.management.system.enums.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for recording a member payment.
 *
 * Admin provides member ID, membership ID, amount,
 * payment date, and method. Status defaults to PAID on creation.
 */
@Data
public class MemberPaymentRequestDTO {

    @NotNull(message = "Member ID is required")
    @Positive(message = "Member ID must be positive")
    private Long memberId;

    @NotNull(message = "Membership ID is required")
    @Positive(message = "Membership ID must be positive")
    private Long membershipId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @DecimalMax(value = "999999.99", message = "Amount cannot exceed 999999.99")
    private Double amount;

    @NotNull(message = "Payment date is required")
    @PastOrPresent(message = "Payment date cannot be in the future")
    private LocalDate paymentDate;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Size(max = 255, message = "Notes cannot exceed 255 characters")
    private String notes;
}