package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.MembershipRequestDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.enums.MembershipStatus;
import org.springframework.data.domain.Page;

/**
 * Service interface for managing memberships.
 *
 * Handles creation, updates, retrieval, filtering, and deletion of memberships.
 */
public interface MembershipService {

    // Create a new membership for a member with a selected plan
    MembershipResponseDTO createMembership(Long memberId, Long planId, MembershipRequestDTO membershipRequestDto);

    // Update an existing membership (e.g., extend or modify dates)
    MembershipResponseDTO updateMembership(Long membershipId, MembershipRequestDTO membershipRequestDto);

    // Get membership details for a specific member
    MembershipResponseDTO getMembershipByMemberId(Long memberId);

    // Get all memberships with pagination and sorting
    Page<MembershipResponseDTO> getAllMemberships(int page, int size, String sortBy, String sortDir);

    // Get memberships filtered by status (ACTIVE, EXPIRED, UPCOMING)
    Page<MembershipResponseDTO> getMembershipsByStatus(MembershipStatus status, int page, int size, String sortBy, String sortDir);

    // Delete a membership by ID
    String deleteMembership(Long membershipId);
}