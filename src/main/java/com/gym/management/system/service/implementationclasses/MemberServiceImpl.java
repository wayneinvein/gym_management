package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MemberDTOMapper;
import com.gym.management.system.dto.request.MemberRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.Trainers;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.TrainerRepository;
import com.gym.management.system.security.SecurityUtils;
import com.gym.management.system.service.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for managing gym members.
 *
 * Handles CRUD operations, trainer assignment, pagination,
 * and fetching logged-in user's profile.
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final MemberDTOMapper memberDtoMapper;
    private final SecurityUtils securityUtils;

    @Override
    public Page<MemberResponseDTO> getAllMembers(int page, int size, String sortBy, String sortDir) {

        // Define sorting direction based on input
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // Create pageable object for pagination
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Members> membersPage = memberRepository.findAll(pageable);

        // If no members exist, throw exception
        if (membersPage.isEmpty()) {
            throw new NotFoundException("Members not found");
        }

        // Convert entity page to DTO page
        return membersPage.map(memberDtoMapper::toResponse);
    }

    @Override
    public MemberResponseDTO getMemberById(Long id) {

        // Fetch member or throw exception if not found
        Members member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + id));

        return memberDtoMapper.toResponse(member);
    }

    @Override
    public MemberResponseDTO addMember(MemberRequestDTO memberRequestDto) {

        // Convert DTO to entity
        Members member = memberDtoMapper.toEntity(memberRequestDto);

        // Save member in database
        Members saved = memberRepository.save(member);

        return memberDtoMapper.toResponse(saved);
    }

    @Override
    public MemberResponseDTO updateMember(Long id, MemberRequestDTO memberRequestDto) {

        Optional<Members> existing = memberRepository.findById(id);

        if (existing.isPresent()) {

            Members m = existing.get();

            // Update fields
            m.setMemberName(memberRequestDto.getMemberName());
            m.setMemberGender(memberRequestDto.getMemberGender());
            m.setPhoneNumber(memberRequestDto.getPhoneNumber());

            // Save updated entity
            Members updated = memberRepository.save(m);

            return memberDtoMapper.toResponse(updated);
        }

        throw new NotFoundException("Member not found with id:" + id);
    }

    @Override
    public void deleteMember(Long id) {

        Optional<Members> existing = memberRepository.findById(id);

        if (existing.isPresent()) {
            memberRepository.deleteById(id);
        } else {
            throw new NotFoundException("Member not found with id:" + id);
        }
    }

    @Override
    public MemberResponseDTO assignTrainer(Long memberId, Long trainerId) {

        // Fetch member
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + memberId));

        // Fetch trainer
        Trainers trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new NotFoundException("Trainer not found with id: " + trainerId));

        // Assign trainer to member
        member.setTrainer(trainer);

        // Save relationship
        Members updated = memberRepository.save(member);

        return memberDtoMapper.toResponse(updated);
    }

    @Override
    public MemberResponseDTO getMyProfile() {

        // Get currently authenticated username from security context
        String username = securityUtils.getCurrentUsername();

        if (username == null) {
            throw new RuntimeException("User not authenticated");
        }

        // Fetch member linked to logged-in user
        Members member = memberRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return memberDtoMapper.toResponse(member);
    }
}