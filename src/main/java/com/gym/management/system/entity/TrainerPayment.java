package com.gym.management.system.entity;

import com.gym.management.system.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a salary payment made to a trainer.
 *
 * A trainer payment is recorded when admin pays a trainer's monthly salary.
 * Each record represents one month's salary payment.
 * Records are never deleted — only status is updated.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"trainer"})
public class TrainerPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    // Trainer who received this payment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    // Amount paid — may differ from base salary (bonus, deductions etc)
    @Column(nullable = false)
    private double amount;

    // Date the salary was paid
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    // Which month this salary is for (e.g., "APRIL_2026")
    // Stored as string for simplicity and readability
    @Column(name = "salary_month", nullable = false)
    private String salaryMonth;

    // Current status of this payment
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    // Optional notes (e.g., bonus details, deduction reason)
    @Column(length = 255)
    private String notes;

    // Automatically set when payment record is created
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}