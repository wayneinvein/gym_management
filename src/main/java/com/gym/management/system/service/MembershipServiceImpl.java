package com.gym.management.system.service;

import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.Membership;
import com.gym.management.system.entity.MembershipPlan;
import com.gym.management.system.enums.MembershipStatus;
import com.gym.management.system.exception.*;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.MembershipPlanRepository;
import com.gym.management.system.repository.MembershipRepository;
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

    @Override
    public Membership createMembership(Long memberId, Long planId, Membership membership) {

        // Validate member exists
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new MemberNotFoundException("Member not found with ID: " + memberId));

        // Validate membership plan exists
        MembershipPlan membershipPlan = membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new PlanDoNotExistException("Plan not found"));

        // Prevent duplicate membership
        Membership existingMembership =
                membershipRepository.findByMemberMemberId(memberId);

        if (existingMembership != null) {
            throw new MembershipAlreadyPresentException(
                    "Membership for member ID " + memberId + " is already present."
            );
        }

        // Attach member
        membership.setMember(member);

        // Default start date if null
        if (membership.getStartDate() == null) {
            membership.setStartDate(LocalDate.now());
        }

        // Attach plan
        membership.setPlan(membershipPlan);

        // Auto derive end date from plan
        membership.setEndDate(
                membership.getStartDate().plusDays(membershipPlan.getDurationDays())
        );

        // Now safely calculate status
        membership.setStatus(
                calculateStatus(membership.getStartDate(), membership.getEndDate())
        );

        return membershipRepository.save(membership);
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
    public Membership updateMembership(Long membershipId, Membership updatedMembership) {
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