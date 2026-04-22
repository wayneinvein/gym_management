package com.gym.management.system.repository;

import com.gym.management.system.entity.Membership;
import com.gym.management.system.enums.MembershipStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Membership entity.
 */
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    // Get all memberships for a specific member
    List<Membership> findByMemberMemberId(Long memberId);

    // Get active membership for a specific member
    Optional<Membership> findByMemberMemberIdAndStatus(Long memberId, MembershipStatus status);

    // Get memberships by status with pagination
    Page<Membership> findByStatus(MembershipStatus status, Pageable pageable);

    // Get memberships expiring before a given date — used for expiry alerts
    List<Membership> findByEndDateBeforeAndStatus(LocalDate date, MembershipStatus status);
}