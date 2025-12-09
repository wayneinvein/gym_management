package com.gym.management.system.service;

import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.Membership;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.MembershipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;

    public MembershipServiceImpl(MembershipRepository membershipRepository,
                                 MemberRepository memberRepository) {
        this.membershipRepository = membershipRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Membership createMembership(Long memberId, Membership membership) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));

        membership.setMember(member);
        return membershipRepository.save(membership);
    }

    @Override
    public Membership updateMembership(Long membershipId, Membership updatedMembership) {
        Membership existingMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found with ID: " + membershipId));

        existingMembership.setType(updatedMembership.getType());
        existingMembership.setStartDate(updatedMembership.getStartDate());
        existingMembership.setEndDate(updatedMembership.getEndDate());
        existingMembership.setPrice(updatedMembership.getPrice());
        existingMembership.setStatus(updatedMembership.getStatus());

        return membershipRepository.save(existingMembership);
    }

    @Override
    public Membership getMembershipByMemberId(Long memberId) {
        Membership membership = membershipRepository.findByMember_MemberId(memberId);

        if (membership == null) {
            throw new RuntimeException("No membership found for member ID: " + memberId);
        }

        return membership;
    }

    @Override
    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    @Override
    public List<Membership> getMembershipsByStatus(String status) {
        return membershipRepository.findByStatus(status);
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
