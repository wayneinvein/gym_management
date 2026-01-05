package com.gym.management.system.service.implememtationclasses;

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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

        // 1️⃣ Validate Member Exists
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new MemberNotFoundException("Member not found with ID: " + memberId));

        // 2️⃣ Validate Plan Exists
        MembershipPlan membershipPlan = membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new PlanDoNotExistException("Plan not found"));

        // 3️⃣ Prevent Duplicate Membership For Same Member
        Membership existingMembership = membershipRepository.findByMemberMemberId(memberId);
        if (existingMembership != null) {
            throw new MembershipAlreadyPresentException(
                    "Membership for member ID " + memberId + " is already present.");
        }

        // 4️⃣ Handle Start Date (Use today's date if null)
        LocalDate startDate = (membershipRequestDto.getStartDate() != null)
                ? membershipRequestDto.getStartDate()
                : LocalDate.now();

        // 5️⃣ Auto Calculate End Date
        LocalDate endDate = startDate.plusDays(membershipPlan.getDurationDays());

        // 6️⃣ Determine Status
        MembershipStatus status = calculateStatus(startDate, endDate);

        // 7️⃣ Convert DTO -> Entity
        Membership convertedMembership = membershipDtoMapper.toEntity(membershipRequestDto);

        // 8️⃣ Override Important Fields (Never trust request)
        convertedMembership.setMember(member);
        convertedMembership.setPlan(membershipPlan);
        convertedMembership.setStartDate(startDate);
        convertedMembership.setEndDate(endDate);
        convertedMembership.setStatus(status);

        // 9️⃣ Save
        Membership savedMembership = membershipRepository.save(convertedMembership);

        // 🔟 Convert Entity -> Response DTO
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
    public MembershipResponseDto updateMembership(Long membershipId, MembershipRequestDto updatedMembership) {

        Membership existingMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() ->
                        new MembershipNotFoundException("Membership not found with ID: " + membershipId));

        // --- Update Start Date ---
        if (updatedMembership.getStartDate() != null) {
            existingMembership.setStartDate(updatedMembership.getStartDate());
        }

        // --- Update End Date ---
        if (updatedMembership.getEndDate() != null) {
            existingMembership.setEndDate(updatedMembership.getEndDate());
        }

        // --- Recalculate / Or Accept Status ---
        if (updatedMembership.getStatus() != null) {
            existingMembership.setStatus(updatedMembership.getStatus());
        } else {
            existingMembership.setStatus(
                    calculateStatus(existingMembership.getStartDate(), existingMembership.getEndDate())
            );
        }

        Membership savedMembership = membershipRepository.save(existingMembership);

        // convert entity to response dto
        return membershipDtoMapper.toResponse(savedMembership);
    }


    @Override
    public MembershipResponseDto getMembershipByMemberId(Long memberId) {

        Membership membership = membershipRepository.findByMember_MemberId(memberId);

        if (membership == null) {
            throw new MembershipNotFoundException(
                    "No membership found for member ID: " + memberId
            );
        }

        return  membershipDtoMapper.toResponse(membership);
    }

    @Override
    public List<MembershipResponseDto> getAllMemberships() {
        return membershipRepository.findAll()
                .stream()
                .map(membershipDtoMapper::toResponse)
                .toList();
    }

    @Override
    public List<MembershipResponseDto> getMembershipsByStatus(MembershipStatus status) {

        List<Membership> memberships = membershipRepository.findByStatus(status);

        if (memberships.isEmpty()) {
            throw new MembershipNotFoundException(
                    "Membership with status '" + status + "' not found"
            );
        }

        return memberships.stream()
                .map(membershipDtoMapper::toResponse)
                .toList();
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