package com.gym.management.system.service;

import com.gym.management.system.entity.Membership;
import com.gym.management.system.enums.MembershipStatus;

import java.util.List;

public interface MembershipService {

    Membership createMembership(Long memberId, Long planId, Membership membership);

    Membership updateMembership(Long membershipId, Membership membership);

    Membership getMembershipByMemberId(Long memberId);

    List<Membership> getAllMemberships();

    List<Membership> getMembershipsByStatus(MembershipStatus status);

    String deleteMembership(Long membershipId);
}