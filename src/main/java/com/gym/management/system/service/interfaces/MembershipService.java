package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.MembershipRequestDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.enums.MembershipStatus;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service interface for managing memberships.
 */
public interface MembershipService {

    // Create a new membership for a member with a selected plan
    MembershipResponseDTO createMembership(Long memberId, Long planId, MembershipRequestDTO dto);

    // Get all memberships for a specific member (full history)
    List<MembershipResponseDTO> getMembershipsByMemberId(Long memberId);

    // Get current active membership for a member
    MembershipResponseDTO getActiveMembership(Long memberId);

    // Get all memberships with pagination
    Page<MembershipResponseDTO> getAllMemberships(int page, int size, String sortBy, String sortDir);

    // Get memberships filtered by status
    Page<MembershipResponseDTO> getMembershipsByStatus(MembershipStatus status, int page, int size, String sortBy, String sortDir);

    // Cancel a membership
    MembershipResponseDTO cancelMembership(Long membershipId);

    // Get memberships expiring in the next N days — used for dashboard alerts
    List<MembershipResponseDTO> getExpiringMemberships(int days);
}