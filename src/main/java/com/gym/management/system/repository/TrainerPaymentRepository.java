package com.gym.management.system.repository;

import com.gym.management.system.entity.TrainerPayment;
import com.gym.management.system.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

/**
 * Repository for TrainerPayment entity.
 */
@Repository
public interface TrainerPaymentRepository extends JpaRepository<TrainerPayment, Long> {

    // Get all payments for a specific trainer
    List<TrainerPayment> findByTrainerTrainerId(Long trainerId);

    // Get payments by status with pagination
    Page<TrainerPayment> findByStatus(PaymentStatus status, Pageable pageable);

    // Check if a payment already exists for a trainer for a specific month
    // Prevents duplicate salary payments for the same month
    Optional<TrainerPayment> findByTrainerTrainerIdAndSalaryMonth(Long trainerId, String salaryMonth);

    // Get all payments for a specific month
    List<TrainerPayment> findBySalaryMonth(String salaryMonth);

    // Sum of all paid trainer salaries in a date range — used for monthly expenses
    @Query("SELECT SUM(p.amount) FROM TrainerPayment p WHERE p.status = :status AND p.paymentDate BETWEEN :startDate AND :endDate")
    Double sumAmountByStatusAndPaymentDateBetween(
            @Param("status") PaymentStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}