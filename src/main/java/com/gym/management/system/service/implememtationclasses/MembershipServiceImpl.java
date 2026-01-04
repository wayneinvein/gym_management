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
    private MembershipDtoMapper membershipDtoMapper;


    @Override
    public MembershipResponseDto createMembership(
            Long memberId,
            Long planId,
            MembershipRequestDto membershipRequestDto) {

        // --------------------------------------------------------
        // 1️⃣ Validate Member Exists
        // --------------------------------------------------------
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new MemberNotFoundException("Member not found with ID: " + memberId));

        // --------------------------------------------------------
        // 2️⃣ Validate Plan Exists
        // --------------------------------------------------------
        MembershipPlan membershipPlan = membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new PlanDoNotExistException("Plan not found"));

        // --------------------------------------------------------
        // 3️⃣ Prevent Duplicate Membership For Same Member
        // --------------------------------------------------------
        Membership existingMembership = membershipRepository.findByMemberMemberId(memberId);
        if (existingMembership != null) {
            throw new MembershipAlreadyPresentException(
                    "Membership for member ID " + memberId + " is already present."
            );
        }

        // --------------------------------------------------------
        // 4️⃣ Handle Start Date
        // If startDate not sent → use today's date
        // --------------------------------------------------------
        LocalDate startDate = (membershipRequestDto.getStartDate() != null)
                ? membershipRequestDto.getStartDate()
                : LocalDate.now();

        // --------------------------------------------------------
        // 5️⃣ Auto Calculate End Date Based On Plan Duration
        // --------------------------------------------------------
        LocalDate endDate = startDate.plusDays(membershipPlan.getDurationDays());

        // --------------------------------------------------------
        // 6️⃣ Create Membership Entity
        // (Do NOT modify DTO to store entity objects)
        // --------------------------------------------------------
        Membership membership = new Membership();
        membership.setMember(member);                 // set member entity
        membership.setPlan(membershipPlan);           // set plan entity
        membership.setStartDate(startDate);           // final start date
        membership.setEndDate(endDate);               // calculated end date
        membership.setStatus(
                calculateStatus(startDate, endDate)    // calculate status safely
        );

        // --------------------------------------------------------
        // 7️⃣ Save To Database
        // --------------------------------------------------------
        Membership savedMembership = membershipRepository.save(membership);

        // --------------------------------------------------------
        // 8️⃣ Convert Entity → Response DTO
        // (Best practice: never return entity directly)
        // --------------------------------------------------------
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
    public MembershipResponseDto updateMembership(Long membershipId, Membership updatedMembership) {
        Membership existingMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new MembershipNotFoundException("Membership not found with ID: " + membershipId));

        existingMembership.setStartDate(updatedMembership.getStartDate());
        existingMembership.setEndDate(updatedMembership.getEndDate());
        existingMembership.setStatus(updatedMembership.getStatus());

        return membershipRepository.save(existingMembership);
    }

    @Override
    public Membership getMembershipByMemberId(Long memberId) {
        Membership membership = membershipRepository.findByMember_MemberId(memberId);

        if (membership == null) {
            throw new MembershipNotFoundException("No membership found for member ID: " + memberId);
        }

        return membership;
    }

    @Override
    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    @Override
    public List<Membership> getMembershipsByStatus(MembershipStatus status) {
        List<Membership> membershipExisting = membershipRepository.findByStatus(status);
        if(membershipExisting.isEmpty()){
            throw new MembershipNotFoundException("membership with status: '" + status + "' not found");
        }
        return membershipExisting;
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