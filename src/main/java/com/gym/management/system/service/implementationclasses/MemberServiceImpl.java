package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MemberDTOMapper;
import com.gym.management.system.dto.request.MemberRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import com.gym.management.system.entity.Member;
import com.gym.management.system.entity.Trainer;
import com.gym.management.system.entity.User;
import com.gym.management.system.enums.MemberStatus;
import com.gym.management.system.enums.UserRoles;
import com.gym.management.system.exception.AlreadyPresentException;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.exception.TokenException;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.TrainerRepository;
import com.gym.management.system.repository.UserRepository;
import com.gym.management.system.security.SecurityUtils;
import com.gym.management.system.service.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service implementation for managing gym members.
 *
 * Handles CRUD operations, trainer assignment, pagination,
 * auto user account creation, and fetching logged-in member profile.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final MemberDTOMapper memberDtoMapper;
    private final SecurityUtils securityUtils;
    private final PasswordEncoder passwordEncoder;

    @Value("${default.member.password}")
    private String defaultMemberPassword;

    /**
     * Returns a paginated and sorted list of all members.
     * Returns empty page if no members exist — does not throw exception.
     */
    @Override
    public Page<MemberResponseDTO> getAllMembers(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // Returns empty page if no members — empty is valid, not an error
        return memberRepository.findAll(pageable)
                .map(memberDtoMapper::toResponse);
    }

    /**
     * Fetches a single member by their ID.
     * Throws NotFoundException if member does not exist.
     */
    @Override
    public MemberResponseDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + id));
        return memberDtoMapper.toResponse(member);
    }

    /**
     * Creates a new member and automatically creates a login account for them.
     *
     * Auto-created login credentials:
     * - Username = phone number
     * - Password = "Gym@123" (default, member should change after first login)
     * - Role = MEMBER
     *
     * Throws AlreadyPresentException if phone number is already registered.
     */
    @Override
    @Transactional // rolls back both user and member creation if anything fails
    public MemberResponseDTO addMember(MemberRequestDTO memberRequestDto) {

        // Check if phone number is already registered
        if (memberRepository.existsByPhoneNumber(memberRequestDto.getPhoneNumber())) {
            throw new AlreadyPresentException("Member with phone number "
                    + memberRequestDto.getPhoneNumber() + " already exists");
        }

        // Auto create a login account for the member
        // Username = phone number, Password = default "Gym@123"
        User user = new User();
        user.setUsername(memberRequestDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(defaultMemberPassword));
        user.setUserRole(UserRoles.MEMBER);
        User savedUser = userRepository.save(user);

        // Convert DTO to entity and set fields that client should not control
        Member member = memberDtoMapper.toEntity(memberRequestDto);
        member.setUser(savedUser);
        member.setJoinedDate(LocalDate.now());       // always today
        member.setStatus(MemberStatus.ACTIVE);       // always active on creation

        Member saved = memberRepository.save(member);
        return memberDtoMapper.toResponse(saved);
    }

    /**
     * Updates an existing member's details.
     * Only updates fields that are safe to change — status is updated separately.
     * Throws NotFoundException if member does not exist.
     */
    @Override
    public MemberResponseDTO updateMember(Long id, MemberRequestDTO dto) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + id));

        // Update all editable fields
        member.setMemberName(dto.getMemberName());
        member.setMemberGender(dto.getMemberGender());
        member.setPhoneNumber(dto.getPhoneNumber());
        member.setEmail(dto.getEmail());
        member.setAddress(dto.getAddress());
        member.setDateOfBirth(dto.getDateOfBirth());

        return memberDtoMapper.toResponse(memberRepository.save(member));
    }

    /**
     * Deletes a member and their associated login account.
     * Throws NotFoundException if member does not exist.
     */
    @Override
    @Transactional // ensures both member and user are deleted together
    public void deleteMember(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + id));

        // Delete the linked user account as well
        if (member.getUser() != null) {
            userRepository.delete(member.getUser());
        }

        memberRepository.delete(member);
    }

    /**
     * Updates the status of a member (ACTIVE, INACTIVE, SUSPENDED).
     * Used by admin to activate, deactivate, or suspend a member.
     * Throws NotFoundException if member does not exist.
     */
    @Override
    public MemberResponseDTO updateMemberStatus(Long id, MemberStatus status) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + id));

        member.setStatus(status);
        return memberDtoMapper.toResponse(memberRepository.save(member));
    }

    /**
     * Assigns a trainer to a member.
     * Both member and trainer must exist.
     * Throws NotFoundException if either is not found.
     */
    @Override
    public MemberResponseDTO assignTrainer(Long memberId, Long trainerId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + memberId));

        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new NotFoundException("Trainer not found with id: " + trainerId));

        member.setTrainer(trainer);
        return memberDtoMapper.toResponse(memberRepository.save(member));
    }

    /**
     * Returns all members assigned to a specific trainer.
     * Used by admin and trainer to view their assigned members.
     */
    @Override
    public List<MemberResponseDTO> getMembersByTrainer(Long trainerId) {

        // Verify trainer exists before fetching members
        if (!trainerRepository.existsById(trainerId)) {
            throw new NotFoundException("Trainer not found with id: " + trainerId);
        }

        return memberRepository.findByTrainerTrainerId(trainerId)
                .stream()
                .map(memberDtoMapper::toResponse)
                .toList();
    }

    /**
     * Returns the profile of the currently logged-in member.
     * Reads username from Spring Security context.
     * Throws TokenException if user is not authenticated.
     * Throws NotFoundException if no member profile is linked to the account.
     */
    @Override
    public MemberResponseDTO getMyProfile() {

        // Get username of the currently authenticated user from JWT
        String username = securityUtils.getCurrentUsername();

        if (username == null) {
            throw new TokenException("User not authenticated");
        }

        // Find the member linked to this login account
        Member member = memberRepository.findByUserUsername(username)
                .orElseThrow(() -> new NotFoundException("Member profile not found for user: " + username));

        return memberDtoMapper.toResponse(member);
    }

    /**
     * Allows logged-in member to update their own profile.
     * Member can only update their own details — not other members.
     */
    @Override
    public MemberResponseDTO updateMyProfile(String username, MemberRequestDTO dto) {

        log.info("Member profile update request for username: {}", username);

        Member member = memberRepository.findByUserUsername(username)
                .orElseThrow(() -> new NotFoundException("Member profile not found"));

        // Check if new phone number is already taken by another member
        if (!member.getPhoneNumber().equals(dto.getPhoneNumber()) &&
                memberRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new AlreadyPresentException("Phone number already registered");
        }

        member.setMemberName(dto.getMemberName());
        member.setMemberGender(dto.getMemberGender());
        member.setPhoneNumber(dto.getPhoneNumber());
        member.setEmail(dto.getEmail());
        member.setAddress(dto.getAddress());
        member.setDateOfBirth(dto.getDateOfBirth());

        log.info("Member profile updated successfully for username: {}", username);
        return memberDtoMapper.toResponse(memberRepository.save(member));
    }
}