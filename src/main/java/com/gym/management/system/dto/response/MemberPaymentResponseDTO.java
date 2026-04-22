package com.gym.management.system.dto.response;

import com.gym.management.system.enums.PaymentMethod;
import com.gym.management.system.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning payment details in API responses.
 *
 * Uses flat fields instead of full entity objects
 * to keep the response clean and avoid circular references.
 */
@Data
public class MemberPaymentResponseDTO {

    // Unique identifier of the payment
    private Long paymentId;

    // Member details — flat fields
    private Long memberId;
    private String memberName;

    // Membership details — flat fields
    private Long membershipId;
    private String planName;

    // Payment details
    private double amount;
    private LocalDate paymentDate;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;

    // Optional notes
    private String notes;

    // When this record was created
    private LocalDateTime createdAt;
}