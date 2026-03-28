package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MembershipDtoMapper;
import com.gym.management.system.dto.request.MembershipRequestDto;
import com.gym.management.system.dto.response.MembershipResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    //dependencies
    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipDtoMapper membershipDtoMapper;

    @Override
    public MembershipResponseDto createMembership(
            Long memberId,
            Long planId,
            MembershipRequestDto membershipRequestDto) {

        // Validate Member Exists
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new NotFoundException("Member not found with ID: " + memberId));

        // Validate Plan Exists
        MembershipPlan membershipPlan = membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new NotFoundException("Plan not found"));

        // Prevent Duplicate Membership For Same Member
        Membership existingMembership = membershipRepository.findByMemberMemberId(memberId);
        if (existingMembership != null) {
            throw new AlreadyPresentException(
                    "Membership for member ID " + memberId + " is already present.");
        }

        // Handle Start Date (Use today's date if null)
        LocalDate startDate = (membershipRequestDto.getStartDate() != null)
                ? membershipRequestDto.getStartDate()
                : LocalDate.now();

        // Auto Calculate End Date
        LocalDate endDate = startDate.plusDays(membershipPlan.getDurationDays());

        // Determine Status
        MembershipStatus status = calculateStatus(startDate, endDate);

        // Convert DTO -> Entity
        Membership convertedMembership = membershipDtoMapper.toEntity(membershipRequestDto);

        // Override Important Fields (Never trust request)
        convertedMembership.setMember(member);
        convertedMembership.setPlan(membershipPlan);
        convertedMembership.setStartDate(startDate);
        convertedMembership.setEndDate(endDate);
        convertedMembership.setStatus(status);

        // Save
        Membership savedMembership = membershipRepository.save(convertedMembership);

        // Convert Entity -> Response DTO
        return membershipDtoMapper.toResponse(savedMembership);
    }



    private MembershipStatus calculateStatus(LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new InvalidInputException("Start date cannot be after end date");
        }

        LocalDate today = LocalDate.now();

        if (today.isBefore(startDate)) {
            return MembershipStatus.UPCOMING;
        }
        else if (!today.isAfter(endDate)) {
            return MembershipStatus.ACTIVE;
        }
        else {
            return MembershipStatus.EXPIRED;
        }
    }


    @Override
    public MembershipResponseDto updateMembership(Long membershipId,
                                                  MembershipRequestDto updatedMembership) {

        Membership existingMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Membership not found with ID: " + membershipId));

        boolean recalculate = false;

        // --- Update Start Date ---
        if (updatedMembership.getStartDate() != null) {
            existingMembership.setStartDate(updatedMembership.getStartDate());
            recalculate = true;
        }

        // --- Recalculate End Date + Status if needed ---
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

    private LocalDate calculateEndDate(LocalDate startDate, int durationDays) {
        return startDate.plusDays(durationDays);
    }


    @Override
    public MembershipResponseDto getMembershipByMemberId(Long memberId) {

        Membership membership = membershipRepository.findByMember_MemberId(memberId);

        if (membership == null) {
            throw new NotFoundException(
                    "No membership found for member ID: " + memberId
            );
        }

        return  membershipDtoMapper.toResponse(membership);
    }

    @Override
    public Page<MembershipResponseDto> getAllMemberships(int page, int size, String sortBy, String sortDir) {

        //sorting
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        //pagination
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Membership> membershipPage = membershipRepository.findAll(pageable);

        if (membershipPage.isEmpty()) {
            throw new NotFoundException("Membership not found"); }

        return membershipPage.map(membershipDtoMapper::toResponse);
    }

    @Override
    public Page<MembershipResponseDto> getMembershipsByStatus(MembershipStatus status, int page, int size, String sortBy, String sortDir) {

        //sorting
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        //pagination
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Membership> membershipsPage = membershipRepository.findByStatus(status, pageable);

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