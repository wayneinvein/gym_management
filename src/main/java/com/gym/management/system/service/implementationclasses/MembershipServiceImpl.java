package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MembershipDTOMapper;
import com.gym.management.system.dto.request.MembershipRequestDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.Membership;
import com.gym.management.system.entity.MembershipPlan;
import com.gym.management.system.enums.MembershipStatus;
import com.gym.management.system.exception.*;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.MembershipPlanRepository;
import com.gym.management.system.repository.MembershipRepository;
import com.gym.management.system.service.interfaces.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service implementation for managing memberships.
 *
 * Handles creation, update, retrieval, filtering, and deletion of memberships.
 * Also manages business rules like status calculation and date validation.
 */
@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipDTOMapper membershipDtoMapper;

    @Override
    public MembershipResponseDTO createMembership(
            Long memberId,
            Long planId,
            MembershipRequestDTO membershipRequestDto) {

        // Validate member existence
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new NotFoundException("Member not found with ID: " + memberId));

        // Validate plan existence
        MembershipPlan membershipPlan = membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new NotFoundException("Plan not found"));

        // Prevent duplicate membership for same member
        Membership existingMembership = membershipRepository.findByMemberMemberId(memberId);
        if (existingMembership != null) {
            throw new AlreadyPresentException(
                    "Membership for member ID " + memberId + " is already present."
            );
        }

        // Set start date (default to today if not provided)
        LocalDate startDate = (membershipRequestDto.getStartDate() != null)
                ? membershipRequestDto.getStartDate()
                : LocalDate.now();

        // Calculate end date based on plan duration
        LocalDate endDate = startDate.plusDays(membershipPlan.getDurationDays());

        // Determine membership status based on dates
        MembershipStatus status = calculateStatus(startDate, endDate);

        // Convert DTO to entity
        Membership membership = membershipDtoMapper.toEntity(membershipRequestDto);

        // Override critical fields (never trust client input)
        membership.setMember(member);
        membership.setPlan(membershipPlan);
        membership.setStartDate(startDate);
        membership.setEndDate(endDate);
        membership.setStatus(status);

        // Save membership
        Membership savedMembership = membershipRepository.save(membership);

        return membershipDtoMapper.toResponse(savedMembership);
    }

    /**
     * Determines membership status based on start and end date.
     */
    private MembershipStatus calculateStatus(LocalDate startDate, LocalDate endDate) {

        // Validate date logic
        if (startDate.isAfter(endDate)) {
            throw new InvalidInputException("Start date cannot be after end date");
        }

        LocalDate today = LocalDate.now();

        if (today.isBefore(startDate)) {
            return MembershipStatus.UPCOMING;
        } else if (!today.isAfter(endDate)) {
            return MembershipStatus.ACTIVE;
        } else {
            return MembershipStatus.EXPIRED;
        }
    }

    @Override
    public MembershipResponseDTO updateMembership(Long membershipId,
                                                  MembershipRequestDTO updatedMembership) {

        // Fetch membership
        Membership existingMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() ->
                        new NotFoundException("Membership not found with ID: " + membershipId));

        boolean recalculate = false;

        // Update start date if provided
        if (updatedMembership.getStartDate() != null) {
            existingMembership.setStartDate(updatedMembership.getStartDate());
            recalculate = true;
        }

        // Recalculate end date and status if start date changed
        if (recalculate) {
            MembershipPlan plan = existingMembership.getPlan();

            existingMembership.setEndDate(
                    calculateEndDate(
                            existingMembership.getStartDate(),
                            plan.getDurationDays()
                    )
            );

            existingMembership.setStatus(
                    calculateStatus(
                            existingMembership.getStartDate(),
                            existingMembership.getEndDate()
                    )
            );
        }

        Membership savedMembership = membershipRepository.save(existingMembership);
        return membershipDtoMapper.toResponse(savedMembership);
    }

    /**
     * Calculates end date using start date and duration.
     */
    private LocalDate calculateEndDate(LocalDate startDate, int durationDays) {
        return startDate.plusDays(durationDays);
    }

    @Override
    public MembershipResponseDTO getMembershipByMemberId(Long memberId) {

        Membership membership = membershipRepository.findByMember_MemberId(memberId);

        if (membership == null) {
            throw new NotFoundException(
                    "No membership found for member ID: " + memberId
            );
        }

        return membershipDtoMapper.toResponse(membership);
    }

    @Override
    public Page<MembershipResponseDTO> getAllMemberships(int page, int size, String sortBy, String sortDir) {

        // Sorting configuration
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // Pagination setup
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Membership> membershipPage = membershipRepository.findAll(pageable);

        if (membershipPage.isEmpty()) {
            throw new NotFoundException("Membership not found");
        }

        return membershipPage.map(membershipDtoMapper::toResponse);
    }

    @Override
    public Page<MembershipResponseDTO> getMembershipsByStatus(MembershipStatus status,
                                                              int page,
                                                              int size,
                                                              String sortBy,
                                                              String sortDir) {

        // Sorting configuration
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // Pagination setup
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Membership> membershipsPage =
                membershipRepository.findByStatus(status, pageable);

        if (membershipsPage.isEmpty()) {
            throw new NotFoundException(
                    "Membership with status '" + status + "' not found"
            );
        }

        return membershipsPage.map(membershipDtoMapper::toResponse);
    }

    @Override
    public String deleteMembership(Long membershipId) {

        if (!membershipRepository.existsById(membershipId)) {
            return "Membership not found with ID: " + membershipId;
        }

        membershipRepository.deleteById(membershipId);
        return "Membership deleted successfully";
    }
}