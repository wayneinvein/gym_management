package com.gym.management.system.controller;

import com.gym.management.system.dto.request.TrainerPaymentRequestDTO;
import com.gym.management.system.dto.response.TrainerPaymentResponseDTO;
import com.gym.management.system.enums.PaymentStatus;
import com.gym.management.system.service.interfaces.TrainerPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for managing trainer salary payments.
 *
 * Access control:
 * - ADMIN → full access to all endpoints
 */
@Tag(name = "Trainer Payment APIs", description = "Operations related to trainer salary payments")
@RestController
@RequestMapping("/api/payments/trainers")
@RequiredArgsConstructor
public class TrainerPaymentController {

    private final TrainerPaymentService trainerPaymentService;

    /**
     * Records a new salary payment for a trainer.
     */
    @Operation(summary = "Record trainer payment", description = "Records a salary payment for a trainer")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerPaymentResponseDTO> recordPayment(
            @Valid @RequestBody TrainerPaymentRequestDTO dto) {
        return new ResponseEntity<>(
                trainerPaymentService.recordPayment(dto),
                HttpStatus.CREATED
        );
    }

    /**
     * Returns all trainer payments with pagination.
     */
    @Operation(summary = "Get all trainer payments")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<TrainerPaymentResponseDTO>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "paymentId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(
                trainerPaymentService.getAllPayments(page, size, sortBy, sortDir)
        );
    }

    /**
     * Returns all payments for a specific trainer.
     */
    @Operation(summary = "Get payments by trainer")
    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrainerPaymentResponseDTO>> getPaymentsByTrainer(
            @PathVariable Long trainerId) {
        return ResponseEntity.ok(trainerPaymentService.getPaymentsByTrainer(trainerId));
    }

    /**
     * Returns a single payment by ID.
     */
    @Operation(summary = "Get payment by ID")
    @GetMapping("/{paymentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerPaymentResponseDTO> getPaymentById(
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(trainerPaymentService.getPaymentById(paymentId));
    }

    /**
     * Returns payments filtered by status.
     */
    @Operation(summary = "Get payments by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<TrainerPaymentResponseDTO>> getPaymentsByStatus(
            @PathVariable PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "paymentId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(
                trainerPaymentService.getPaymentsByStatus(status, page, size, sortBy, sortDir)
        );
    }

    /**
     * Updates payment status (PAID, PENDING, OVERDUE).
     */
    @Operation(summary = "Update payment status")
    @PatchMapping("/{paymentId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerPaymentResponseDTO> updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(
                trainerPaymentService.updatePaymentStatus(paymentId, status)
        );
    }

    /**
     * Returns all payments for a specific salary month.
     * Used to view total salary expenses for a month.
     */
    @Operation(summary = "Get payments by month", description = "Returns all trainer payments for a given month e.g. APRIL_2026")
    @GetMapping("/month/{salaryMonth}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrainerPaymentResponseDTO>> getPaymentsByMonth(
            @PathVariable String salaryMonth) {
        return ResponseEntity.ok(trainerPaymentService.getPaymentsByMonth(salaryMonth));
    }

    // Returns all PENDING and OVERDUE trainer salary payments
    @GetMapping("/pending-dues")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrainerPaymentResponseDTO>> getPendingDues() {
        return ResponseEntity.ok(trainerPaymentService.getPendingDues());
    }
}