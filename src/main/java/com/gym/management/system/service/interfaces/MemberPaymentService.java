package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.MemberPaymentRequestDTO;
import com.gym.management.system.dto.response.MemberPaymentResponseDTO;
import com.gym.management.system.enums.PaymentStatus;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service interface for managing member payments.
 */
public interface MemberPaymentService {

    // Record a new payment
    MemberPaymentResponseDTO recordPayment(MemberPaymentRequestDTO dto);

    // Get all payments with pagination
    Page<MemberPaymentResponseDTO> getAllPayments(int page, int size, String sortBy, String sortDir);

    // Get all payments for a specific member
    List<MemberPaymentResponseDTO> getPaymentsByMember(Long memberId);

    // Get payments filtered by status
    Page<MemberPaymentResponseDTO> getPaymentsByStatus(PaymentStatus status, int page, int size, String sortBy, String sortDir);

    // Get payment by ID
    MemberPaymentResponseDTO getPaymentById(Long paymentId);

    // Update payment status (e.g., mark as PAID, OVERDUE)
    MemberPaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentStatus status);
}