package com.gym.management.system.repository;

import com.gym.management.system.entity.MemberPayment;
import com.gym.management.system.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}