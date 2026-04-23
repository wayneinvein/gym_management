package com.gym.management.system.dto.response;

import com.gym.management.system.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning trainer payment details in API responses.
 *
 * Uses flat fields instead of full entity objects
 * to keep the response clean and avoid circular references.
 */
@Data
public class TrainerPaymentResponseDTO {

    // Unique identifier of the payment
    private Long paymentId;

    // Trainer details — flat fields
    private Long trainerId;
    private String trainerName;

    // Payment details
    private double amount;
    private LocalDate paymentDate;

    // Month this salary covers
    private String salaryMonth;

    // Current status of the payment
    private PaymentStatus status;

    // Optional notes
    private String notes;

    // When this record was created
    private LocalDateTime createdAt;
}