package com.gym.management.system.dto.request;

import com.gym.management.system.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;

/**
 * DTO for recording a trainer salary payment.
 *
 * Admin provides trainer ID, amount, payment date,
 * salary month, and status.
 */
@Data
public class TrainerPaymentRequestDTO {

    // Trainer receiving the payment
    @NotNull(message = "Trainer ID is required")
    private Long trainerId;

    // Amount paid — must be positive
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    // Date the payment was made — null if status is PENDING
    private LocalDate paymentDate;

    // Month this salary is for (e.g., "APRIL_2026")
    @NotBlank(message = "Salary month is required")
    private String salaryMonth;

    // Payment status — PAID or PENDING
    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    // Optional notes
    private String notes;
}