package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.MembershipRequestDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.enums.MembershipStatus;
import org.springframework.data.domain.Page;

public interface MembershipService {

    MembershipResponseDTO createMembership(Long memberId, Long planId, MembershipRequestDTO membershipRequestDto);

    MembershipResponseDTO updateMembership(Long membershipId, MembershipRequestDTO membershipRequestDto);

    MembershipResponseDTO getMembershipByMemberId(Long memberId);

    Page<MembershipResponseDTO> getAllMemberships(int page, int size, String sortBy, String sortDir);

    Page<MembershipResponseDTO> getMembershipsByStatus(MembershipStatus status, int page, int size, String sortBy, String sortDir);

    String deleteMembership(Long membershipId);
}