package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.TrainerPaymentDTOMapper;
import com.gym.management.system.dto.request.TrainerPaymentRequestDTO;
import com.gym.management.system.dto.response.TrainerPaymentResponseDTO;
import com.gym.management.system.entity.Trainer;
import com.gym.management.system.entity.TrainerPayment;
import com.gym.management.system.enums.PaymentStatus;
import com.gym.management.system.exception.AlreadyPresentException;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.TrainerPaymentRepository;
import com.gym.management.system.repository.TrainerRepository;
import com.gym.management.system.service.interfaces.TrainerPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service implementation for managing trainer salary payments.
 *
 * Handles payment recording, retrieval by trainer/month/status,
 * and payment status updates.
 */
@Service
@RequiredArgsConstructor
public class TrainerPaymentServiceImpl implements TrainerPaymentService {

    private final TrainerPaymentRepository trainerPaymentRepository;
    private final TrainerRepository trainerRepository;
    private final TrainerPaymentDTOMapper trainerPaymentDTOMapper;

    /**
     * Records a new salary payment for a trainer.
     *
     * Throws NotFoundException if trainer not found.
     * Throws AlreadyPresentException if payment for same trainer
     * and same month already exists — prevents duplicate salary payments.
     */
    @Override
    public TrainerPaymentResponseDTO recordPayment(TrainerPaymentRequestDTO dto) {

        // Fetch trainer
        Trainer trainer = trainerRepository.findById(dto.getTrainerId())
                .orElseThrow(() -> new NotFoundException("Trainer not found with id: " + dto.getTrainerId()));

        // Prevent duplicate salary payment for same trainer and same month
        trainerPaymentRepository
                .findByTrainerTrainerIdAndSalaryMonth(dto.getTrainerId(), dto.getSalaryMonth())
                .ifPresent(existing -> {
                    throw new AlreadyPresentException("Payment for trainer "
                            + trainer.getTrainerName()
                            + " for month " + dto.getSalaryMonth()
                            + " already exists with status: " + existing.getStatus());
                });

        // Build payment record
        TrainerPayment payment = new TrainerPayment();
        payment.setTrainer(trainer);
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setSalaryMonth(dto.getSalaryMonth());
        payment.setStatus(dto.getStatus());
        payment.setNotes(dto.getNotes());

        return trainerPaymentDTOMapper.toResponse(trainerPaymentRepository.save(payment));
    }

    /**
     * Returns all trainer payments with pagination and sorting.
     */
    @Override
    public Page<TrainerPaymentResponseDTO> getAllPayments(
            int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return trainerPaymentRepository.findAll(PageRequest.of(page, size, sort))
                .map(trainerPaymentDTOMapper::toResponse);
    }

    /**
     * Returns all payments for a specific trainer.
     * Throws NotFoundException if trainer does not exist.
     */
    @Override
    public List<TrainerPaymentResponseDTO> getPaymentsByTrainer(Long trainerId) {

        if (!trainerRepository.existsById(trainerId)) {
            throw new NotFoundException("Trainer not found with id: " + trainerId);
        }

        return trainerPaymentDTOMapper.toResponse(
                trainerPaymentRepository.findByTrainerTrainerId(trainerId)
        );
    }

    /**
     * Returns payments filtered by status with pagination.
     */
    @Override
    public Page<TrainerPaymentResponseDTO> getPaymentsByStatus(
            PaymentStatus status, int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return trainerPaymentRepository.findByStatus(status, PageRequest.of(page, size, sort))
                .map(trainerPaymentDTOMapper::toResponse);
    }

    /**
     * Returns a single payment by ID.
     * Throws NotFoundException if payment does not exist.
     */
    @Override
    public TrainerPaymentResponseDTO getPaymentById(Long paymentId) {

        TrainerPayment payment = trainerPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));

        return trainerPaymentDTOMapper.toResponse(payment);
    }

    /**
     * Updates the status of a trainer payment.
     * Used to mark pending payments as PAID.
     * Throws NotFoundException if payment does not exist.
     */
    @Override
    public TrainerPaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentStatus status) {

        TrainerPayment payment = trainerPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));

        payment.setStatus(status);
        return trainerPaymentDTOMapper.toResponse(trainerPaymentRepository.save(payment));
    }

    /**
     * Returns all payments for a specific salary month.
     * Used by admin to view total salary expenses for a month.
     */
    @Override
    public List<TrainerPaymentResponseDTO> getPaymentsByMonth(String salaryMonth) {
        return trainerPaymentDTOMapper.toResponse(
                trainerPaymentRepository.findBySalaryMonth(salaryMonth)
        );
    }
}