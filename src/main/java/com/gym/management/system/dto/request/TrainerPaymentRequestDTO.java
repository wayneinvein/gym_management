package com.gym.management.system.dto.request;

import com.gym.management.system.enums.PaymentStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for recording a trainer salary payment.
 *
 * Admin provides trainer ID, amount, payment date,
 * salary month, and status.
 */
@Data
public class TrainerPaymentRequestDTO {

    @NotNull(message = "Trainer ID is required")
    @Positive(message = "Trainer ID must be positive")
    private Long trainerId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @DecimalMax(value = "999999.99", message = "Amount cannot exceed 999999.99")
    private Double amount;

    @PastOrPresent(message = "Payment date cannot be in the future")
    private LocalDate paymentDate;

    @NotBlank(message = "Salary month is required")
    @Pattern(
            regexp = "^(JANUARY|FEBRUARY|MARCH|APRIL|MAY|JUNE|JULY|AUGUST|SEPTEMBER|OCTOBER|NOVEMBER|DECEMBER)_\\d{4}$",
            message = "Salary month must be in format MONTH_YEAR e.g. APRIL_2026"
    )
    private String salaryMonth;

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    @Size(max = 255, message = "Notes cannot exceed 255 characters")
    private String notes;
}