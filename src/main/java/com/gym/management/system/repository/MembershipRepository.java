package com.gym.management.system.repository;

import com.gym.management.system.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByMember_MemberId(Long memberId);

    List<Membership> findByStatus(String status);
    Membership findByMemberMemberId(Long memberId);
}
