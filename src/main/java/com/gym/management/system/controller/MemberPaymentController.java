package com.gym.management.system.controller;

import com.gym.management.system.dto.request.MemberPaymentRequestDTO;
import com.gym.management.system.dto.response.MemberPaymentResponseDTO;
import com.gym.management.system.enums.PaymentMethod;
import com.gym.management.system.enums.PaymentStatus;
import com.gym.management.system.service.interfaces.MemberPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing member payments.
 *
 * Access control:
 * - ADMIN  → full access
 * - MEMBER → can only view their own payments
 */
@Tag(name = "Member Payment APIs", description = "Operations related to member payments")
@RestController
@RequestMapping("/api/payments/members")
@RequiredArgsConstructor
public class MemberPaymentController {

    private final MemberPaymentService memberPaymentService;

    /**
     * Records a new payment made by a member.
     */
    @Operation(summary = "Record payment", description = "Records a new member payment")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberPaymentResponseDTO> recordPayment(
            @Valid @RequestBody MemberPaymentRequestDTO dto) {
        return new ResponseEntity<>(
                memberPaymentService.recordPayment(dto),
                HttpStatus.CREATED
        );
    }

    /**
     * Returns all payments with pagination.
     */
    @Operation(summary = "Get all payments")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MemberPaymentResponseDTO>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "paymentId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(
                memberPaymentService.getAllPayments(page, size, sortBy, sortDir)
        );
    }

    /**
     * Returns all payments for a specific member.
     */
    @Operation(summary = "Get payments by member")
    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ResponseEntity<List<MemberPaymentResponseDTO>> getPaymentsByMember(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(memberPaymentService.getPaymentsByMember(memberId));
    }

    /**
     * Returns a single payment by ID.
     */
    @Operation(summary = "Get payment by ID")
    @GetMapping("/{paymentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberPaymentResponseDTO> getPaymentById(
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(memberPaymentService.getPaymentById(paymentId));
    }

    /**
     * Returns payments filtered by status.
     */
    @Operation(summary = "Get payments by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MemberPaymentResponseDTO>> getPaymentsByStatus(
            @PathVariable PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "paymentId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(
                memberPaymentService.getPaymentsByStatus(status, page, size, sortBy, sortDir)
        );
    }

    /**
     * Updates payment status (PAID, PENDING, OVERDUE).
     */
    @Operation(summary = "Update payment status")
    @PatchMapping("/{paymentId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberPaymentResponseDTO> updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(
                memberPaymentService.updatePaymentStatus(paymentId, status)
        );
    }

    /**
     * Marks a pending payment as paid.
     * Admin calls this when member pays at counter.
     */
    @Operation(summary = "Mark payment as paid")
    @PatchMapping("/{paymentId}/pay")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberPaymentResponseDTO> markAsPaid(
            @PathVariable Long paymentId,
            @RequestParam PaymentMethod paymentMethod,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDate,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(
                memberPaymentService.markAsPaid(paymentId, paymentMethod, paymentDate, notes)
        );
    }

    // Returns all PENDING and OVERDUE payments — admin uses this to track unpaid dues
    @GetMapping("/pending-dues")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MemberPaymentResponseDTO>> getPendingDues() {
        return ResponseEntity.ok(memberPaymentService.getPendingDues());
    }
}