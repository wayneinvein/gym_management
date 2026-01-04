package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.MembershipRequestDto;
import com.gym.management.system.dto.response.MembershipResponseDto;
import com.gym.management.system.entity.Membership;
import com.gym.management.system.enums.MembershipStatus;

import java.util.List;

public interface MembershipService {

    MembershipResponseDto createMembership(Long memberId, Long planId, MembershipRequestDto membershipRequestDto);

    MembershipResponseDto updateMembership(Long membershipId, MembershipRequestDto membershipRequestDto);

    MembershipResponseDto getMembershipByMemberId(Long memberId);

    List<MembershipResponseDto> getAllMemberships();

    List<MembershipResponseDto> getMembershipsByStatus(MembershipStatus status);

    String deleteMembership(Long membershipId);
}