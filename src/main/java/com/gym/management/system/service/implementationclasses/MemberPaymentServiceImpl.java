package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MemberPaymentDTOMapper;
import com.gym.management.system.dto.request.MemberPaymentRequestDTO;
import com.gym.management.system.dto.response.MemberPaymentResponseDTO;
import com.gym.management.system.entity.Member;
import com.gym.management.system.entity.MemberPayment;
import com.gym.management.system.entity.Membership;
import com.gym.management.system.enums.PaymentMethod;
import com.gym.management.system.enums.PaymentStatus;
import com.gym.management.system.exception.InvalidInputException;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.MemberPaymentRepository;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.MembershipRepository;
import com.gym.management.system.service.interfaces.MemberPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing member payments.
 *
 * Handles payment recording, retrieval, and status updates.
 * Payment records are never deleted — only status is updated.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberPaymentServiceImpl implements MemberPaymentService {

    private final MemberPaymentRepository memberPaymentRepository;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final MemberPaymentDTOMapper memberPaymentDTOMapper;

    /**
     * Records a new payment made by a member.
     * Payment status defaults to PAID on creation.
     * Throws NotFoundException if member or membership not found.
     */
    @Override
    public MemberPaymentResponseDTO recordPayment(MemberPaymentRequestDTO dto) {

        // Fetch member
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + dto.getMemberId()));

        // Fetch membership
        Membership membership = membershipRepository.findById(dto.getMembershipId())
                .orElseThrow(() -> new NotFoundException("Membership not found with id: " + dto.getMembershipId()));

        // Build payment record
        MemberPayment payment = new MemberPayment();
        payment.setMember(member);
        payment.setMembership(membership);
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setNotes(dto.getNotes());
        payment.setStatus(PaymentStatus.PAID); // always PAID when recorded manually

        return memberPaymentDTOMapper.toResponse(memberPaymentRepository.save(payment));
    }

    /**
     * Returns all payments with pagination and sorting.
     */
    @Override
    public Page<MemberPaymentResponseDTO> getAllPayments(
            int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return memberPaymentRepository.findAll(PageRequest.of(page, size, sort))
                .map(memberPaymentDTOMapper::toResponse);
    }

    /**
     * Returns all payments made by a specific member.
     * Throws NotFoundException if member does not exist.
     */
    @Override
    public List<MemberPaymentResponseDTO> getPaymentsByMember(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException("Member not found with id: " + memberId);
        }

        return memberPaymentDTOMapper.toResponse(
                memberPaymentRepository.findByMemberMemberId(memberId)
        );
    }

    /**
     * Returns payments filtered by status with pagination.
     */
    @Override
    public Page<MemberPaymentResponseDTO> getPaymentsByStatus(
            PaymentStatus status, int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return memberPaymentRepository.findByStatus(status, PageRequest.of(page, size, sort))
                .map(memberPaymentDTOMapper::toResponse);
    }

    /**
     * Returns a single payment by ID.
     * Throws NotFoundException if payment does not exist.
     */
    @Override
    public MemberPaymentResponseDTO getPaymentById(Long paymentId) {

        MemberPayment payment = memberPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));

        return memberPaymentDTOMapper.toResponse(payment);
    }

    /**
     * Updates the status of a payment.
     * Used to mark payments as OVERDUE or update status manually.
     * Throws NotFoundException if payment does not exist.
     */
    @Override
    public MemberPaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentStatus status) {

        MemberPayment payment = memberPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));

        payment.setStatus(status);
        return memberPaymentDTOMapper.toResponse(memberPaymentRepository.save(payment));
    }

    /**
     * Marks a pending payment as paid.
     * Called when admin receives payment from member.
     * Updates payment method, date, and status to PAID.
     */
    @Override
    public MemberPaymentResponseDTO markAsPaid(
            Long paymentId, PaymentMethod method, LocalDate paymentDate, String notes) {

        MemberPayment payment = memberPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));

        // Only pending payments can be marked as paid
        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new InvalidInputException("Payment is already marked as paid");
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setPaymentMethod(method);
        payment.setPaymentDate(paymentDate);
        if (notes != null) {
            payment.setNotes(notes);
        }

        return memberPaymentDTOMapper.toResponse(memberPaymentRepository.save(payment));
    }

    /*
     * Retrieves all member payments that are either PENDING or OVERDUE.
     * PENDING → payment not yet collected after membership was created
     * OVERDUE → admin manually marked it as overdue (membership expired, still unpaid)
     * Maps each payment entity to a response DTO and returns the list.
     */
    @Override
    public List<MemberPaymentResponseDTO> getPendingDues() {
        log.info("Fetching all pending and overdue member payments");

        // Fetch payments that are either PENDING or OVERDUE — both mean money not yet collected
        List<MemberPayment> payments = memberPaymentRepository
                .findByStatusIn(List.of(PaymentStatus.PENDING, PaymentStatus.OVERDUE));

        return payments.stream()
                .map(memberPaymentDTOMapper::toResponse)
                .collect(Collectors.toList());
    }
}