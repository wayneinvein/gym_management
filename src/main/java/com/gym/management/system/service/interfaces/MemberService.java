package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.MemberRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import org.springframework.data.domain.Page;

/**
 * Service interface for managing gym members.
 *
 * Defines core operations like CRUD, trainer assignment,
 * pagination, and fetching logged-in user profile.
 */
public interface MemberService {

    // Fetch all members with pagination and sorting support
    Page<MemberResponseDTO> getAllMembers(int page, int size, String sortBy, String sortDir);

    // Get a single member by ID
    MemberResponseDTO getMemberById(Long id);

    // Create a new member
    MemberResponseDTO addMember(MemberRequestDTO memberRequestDto);

    // Update existing member details
    MemberResponseDTO updateMember(Long id, MemberRequestDTO memberRequestDto);

    // Delete member by ID
    void deleteMember(Long id);

    // Assign a trainer to a member
    MemberResponseDTO assignTrainer(Long memberId, Long trainerId);

    // Get currently logged-in user's member profile
    MemberResponseDTO getMyProfile();
}