package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MembershipDTOMapper;
import com.gym.management.system.dto.request.MembershipRequestDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.dto.response.MembershipSummaryResponseDTO;
import com.gym.management.system.entity.Member;
import com.gym.management.system.entity.MemberPayment;
import com.gym.management.system.entity.Membership;
import com.gym.management.system.entity.MembershipPlan;
import com.gym.management.system.enums.MembershipStatus;
import com.gym.management.system.enums.PaymentStatus;
import com.gym.management.system.exception.InvalidInputException;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.MemberPaymentRepository;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.MembershipPlanRepository;
import com.gym.management.system.repository.MembershipRepository;
import com.gym.management.system.service.interfaces.MembershipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service implementation for managing memberships.
 *
 * Handles subscription creation, history retrieval,
 * cancellation, and expiry tracking.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipDTOMapper membershipDTOMapper;
    private final MemberPaymentRepository memberPaymentRepository;

    /**
     * Creates a new membership for a member.

     * - Validates member and plan exist
     * - Checks plan is active
     * - Cancels any existing active membership before creating new one
     * - End date is auto-calculated from plan duration
     * */
    @Override
    public MembershipResponseDTO createMembership(Long memberId, Long planId, MembershipRequestDTO dto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + memberId));

        MembershipPlan plan = membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new NotFoundException("Plan not found with id: " + planId));

        if (!plan.isActive()) {
            throw new InvalidInputException("Plan '" + plan.getName() + "' is not active");
        }

        // Cancel existing active membership if one exists
        membershipRepository.findByMemberMemberIdAndStatus(memberId, MembershipStatus.ACTIVE)
                .ifPresent(existing -> {
                    existing.setStatus(MembershipStatus.CANCELLED);
                    membershipRepository.save(existing);
                });

        // Create new membership
        Membership membership = new Membership();
        membership.setMember(member);
        membership.setPlan(plan);
        membership.setStartDate(dto.getStartDate());
        membership.setEndDate(dto.getStartDate().plusDays(plan.getDurationDays()));
        membership.setStatus(MembershipStatus.ACTIVE);

        Membership saved = membershipRepository.save(membership);

        // Auto create a PENDING payment record for this membership
        // Admin will update it to PAID when money is received
        MemberPayment payment = new MemberPayment();
        payment.setMember(member);
        payment.setMembership(saved);
        payment.setAmount(plan.getPrice()); // use plan price as amount
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(null);       // not paid yet
        payment.setNotes("Auto-created on membership creation");
        memberPaymentRepository.save(payment);

        return membershipDTOMapper.toResponse(saved);
    }

    /**
     * Returns full membership history for a member.
     * Includes all past and current memberships.
     */
    @Override
    public List<MembershipResponseDTO> getMembershipsByMemberId(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException("Member not found with id: " + memberId);
        }

        return membershipRepository.findByMemberMemberId(memberId)
                .stream()
                .map(membershipDTOMapper::toResponse)
                .toList();
    }

    /**
     * Returns the current active membership for a member.
     * Throws NotFoundException if no active membership exists.
     */
    @Override
    public MembershipResponseDTO getActiveMembership(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException("Member not found with id: " + memberId);
        }

        Membership membership = membershipRepository
                .findByMemberMemberIdAndStatus(memberId, MembershipStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("No active membership found for member id: " + memberId));

        return membershipDTOMapper.toResponse(membership);
    }

    /**
     * Returns all memberships with pagination and sorting.
     */
    @Override
    public Page<MembershipResponseDTO> getAllMemberships(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return membershipRepository.findAll(PageRequest.of(page, size, sort))
                .map(membershipDTOMapper::toResponse);
    }

    /**
     * Returns memberships filtered by status with pagination.
     */
    @Override
    public Page<MembershipResponseDTO> getMembershipsByStatus(
            MembershipStatus status, int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return membershipRepository.findByStatus(status, PageRequest.of(page, size, sort))
                .map(membershipDTOMapper::toResponse);
    }

    /**
     * Cancels an active membership.
     * Throws NotFoundException if membership does not exist.
     * Throws InvalidInputException if membership is already cancelled or expired.
     */
    @Override
    public MembershipResponseDTO cancelMembership(Long membershipId) {

        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new NotFoundException("Membership not found with id: " + membershipId));

        // Can only cancel an active membership
        if (membership.getStatus() != MembershipStatus.ACTIVE) {
            throw new InvalidInputException("Only active memberships can be cancelled");
        }

        membership.setStatus(MembershipStatus.CANCELLED);
        return membershipDTOMapper.toResponse(membershipRepository.save(membership));
    }

    /**
     * Returns memberships expiring within the next N days.
     * Used by dashboard to alert admin of upcoming expirations.
     */
    @Override
    public List<MembershipResponseDTO> getExpiringMemberships(int days) {

        // Get all active memberships expiring before today + N days
        LocalDate expiryThreshold = LocalDate.now().plusDays(days);

        return membershipRepository
                .findByEndDateBeforeAndStatus(expiryThreshold, MembershipStatus.ACTIVE)
                .stream()
                .map(membershipDTOMapper::toResponse)
                .toList();
    }

    /**
     * Returns a complete membership summary for the logged-in member.
     *
     * Calculates days completed, days remaining, and payment status
     * based on the member's current active membership.
     * Throws NotFoundException if member has no active membership.
     */
    @Override
    public MembershipSummaryResponseDTO getMembershipSummary(String username) {

        log.info("Membership summary request for username: {}", username);

        // Find member linked to logged-in user
        Member member = memberRepository.findByUserUsername(username)
                .orElseThrow(() -> new NotFoundException("Member profile not found"));

        // Find active membership
        Membership membership = membershipRepository
                .findByMemberMemberIdAndStatus(member.getMemberId(), MembershipStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("No active membership found"));

        LocalDate today = LocalDate.now();
        LocalDate startDate = membership.getStartDate();
        LocalDate endDate = membership.getEndDate();

        // Calculate day stats
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
        long daysCompleted = ChronoUnit.DAYS.between(startDate, today);
        long daysRemaining = ChronoUnit.DAYS.between(today, endDate);

        // Make sure values don't go negative
        daysCompleted = Math.max(0, daysCompleted);
        daysRemaining = Math.max(0, daysRemaining);

        // Check payment status for this membership
        MemberPayment pendingPayment = memberPaymentRepository
                .findByMembershipMembershipIdAndStatus(
                        membership.getMembershipId(), PaymentStatus.PENDING)
                .orElse(null);

        // Build summary
        MembershipSummaryResponseDTO summary = new MembershipSummaryResponseDTO();
        summary.setPlanName(membership.getPlan().getName());
        summary.setPlanDescription(membership.getPlan().getDescription());
        summary.setPlanPrice(membership.getPlan().getPrice());
        summary.setStartDate(startDate);
        summary.setEndDate(endDate);
        summary.setStatus(membership.getStatus());
        summary.setTotalDays(totalDays);
        summary.setDaysCompleted(daysCompleted);
        summary.setDaysRemaining(daysRemaining);
        summary.setNextPaymentDue(endDate); // next renewal is due on end date
        summary.setPaymentPending(pendingPayment != null);
        summary.setAmountDue(pendingPayment != null ? pendingPayment.getAmount() : 0.0);

        log.info("Membership summary generated for member id: {}", member.getMemberId());
        return summary;
    }
}