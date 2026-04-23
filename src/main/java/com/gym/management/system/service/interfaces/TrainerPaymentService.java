package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.TrainerPaymentRequestDTO;
import com.gym.management.system.dto.response.TrainerPaymentResponseDTO;
import com.gym.management.system.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import java.util.List;

/**
 * Service interface for managing trainer salary payments.
 */
public interface TrainerPaymentService {

    // Record a new trainer payment
    TrainerPaymentResponseDTO recordPayment(TrainerPaymentRequestDTO dto);

    // Get all trainer payments with pagination
    Page<TrainerPaymentResponseDTO> getAllPayments(int page, int size, String sortBy, String sortDir);

    // Get all payments for a specific trainer
    List<TrainerPaymentResponseDTO> getPaymentsByTrainer(Long trainerId);

    // Get payments filtered by status
    Page<TrainerPaymentResponseDTO> getPaymentsByStatus(PaymentStatus status, int page, int size, String sortBy, String sortDir);

    // Get payment by ID
    TrainerPaymentResponseDTO getPaymentById(Long paymentId);

    // Update payment status
    TrainerPaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentStatus status);

    // Get all payments for a specific month (e.g., "APRIL_2026")
    List<TrainerPaymentResponseDTO> getPaymentsByMonth(String salaryMonth);
}