package com.gym.management.system.entity;

import com.gym.management.system.enums.PaymentMethod;
import com.gym.management.system.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a payment made by a member.
 *
 * A payment is recorded when a member pays for a membership subscription.
 * Each payment is linked to a member and their membership.
 * Payment history is preserved — records are never deleted.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"member", "membership"})
public class MemberPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    // Member who made this payment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Membership this payment is for
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership membership;

    // Amount paid
    @Column(nullable = false)
    private double amount;

    // Date the payment was made
    // null when payment is pending
    @Column(name = "payment_date", nullable = true)
    private LocalDate paymentDate;

    // How the payment was made (CASH, CARD, UPI)
    // null when payment is pending
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = true)
    private PaymentMethod paymentMethod;

    // Current status of the payment
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    // Optional notes (e.g., transaction ID, receipt number)
    @Column(length = 255)
    private String notes;

    // Automatically set when payment record is created
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}