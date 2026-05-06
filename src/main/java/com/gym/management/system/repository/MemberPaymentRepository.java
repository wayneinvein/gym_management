package com.gym.management.system.repository;

import com.gym.management.system.entity.MemberPayment;
import com.gym.management.system.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for MemberPayment entity.
 */
@Repository
public interface MemberPaymentRepository extends JpaRepository<MemberPayment, Long> {

    // Get all payments for a specific member
    List<MemberPayment> findByMemberMemberId(Long memberId);

    // Get all payments by status with pagination
    Page<MemberPayment> findByStatus(PaymentStatus status, Pageable pageable);

    // Get all payments for a specific membership
    List<MemberPayment> findByMembershipMembershipId(Long membershipId);

    // Sum of all paid payments in a date range — used for monthly revenue
    @Query("SELECT SUM(p.amount) FROM MemberPayment p WHERE p.status = :status AND p.paymentDate BETWEEN :startDate AND :endDate")
    Double sumAmountByStatusAndPaymentDateBetween(
            @Param("status") PaymentStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Sum of all paid payments of all time — used for total revenue
    @Query("SELECT SUM(p.amount) FROM MemberPayment p WHERE p.status = 'PAID'")
    Double sumAllPaidAmounts();

    // Count payments by status
    long countByStatus(PaymentStatus status);

    // Find all payments by status (non-paginated) — used for overdue list
    List<MemberPayment> findByStatus(PaymentStatus status);

    // Find payment by membership id and status
    Optional<MemberPayment> findByMembershipMembershipIdAndStatus(Long membershipId, PaymentStatus status);

    // Fetch all payments whose status is in the given list (e.g. PENDING, OVERDUE)
    List<MemberPayment> findByStatusIn(List<PaymentStatus> statuses);
}