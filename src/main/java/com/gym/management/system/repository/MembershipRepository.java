package com.gym.management.system.repository;

import com.gym.management.system.entity.Membership;
import com.gym.management.system.enums.MembershipStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByMember_MemberId(Long memberId);

    Page<Membership> findByStatus(MembershipStatus status, Pageable pageable);

    Membership findByMemberMemberId(Long memberId);
}
