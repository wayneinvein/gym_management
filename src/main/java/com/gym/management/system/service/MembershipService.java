package com.gym.management.system.service;

import com.gym.management.system.entity.Membership;
import java.util.List;

public interface MembershipService {

    Membership createMembership(Long memberId, Membership membership);

    Membership updateMembership(Long membershipId, Membership membership);

    Membership getMembershipByMemberId(Long memberId);

    List<Membership> getAllMemberships();

    List<Membership> getMembershipsByStatus(String status);

    String deleteMembership(Long membershipId);
}