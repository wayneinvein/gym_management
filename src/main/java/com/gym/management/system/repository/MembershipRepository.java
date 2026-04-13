package com.gym.management.system.repository;

import com.gym.management.system.entity.Membership;
import com.gym.management.system.enums.MembershipStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Membership entity.
 * Provides database access methods for membership records.
 */
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    // Fetch membership by member ID (using nested property traversal)
    Membership findByMember_MemberId(Long memberId);

    // Fetch memberships by status with pagination support
    Page<Membership> findByStatus(MembershipStatus status, Pageable pageable);

    // Alternative method to fetch membership by member ID (duplicate of above)
    Membership findByMemberMemberId(Long memberId);
}