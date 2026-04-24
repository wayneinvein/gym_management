package com.gym.management.system.controller;

import com.gym.management.system.dto.response.DashboardResponseDTO;
import com.gym.management.system.dto.response.MemberPaymentResponseDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.service.interfaces.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for dashboard and reports.
 *
 * Provides admin with a complete overview of gym operations
 * including members, revenue, expenses, and alerts.
 *
 * Access: ADMIN only
 */
@Tag(name = "Dashboard APIs", description = "Gym overview and reports for admin")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Returns complete gym stats in a single response.
     * Covers members, trainers, attendance, memberships, and financials.
     */
    @Operation(summary = "Get gym stats", description = "Returns complete overview of gym operations")
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardResponseDTO> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

    /**
     * Returns memberships expiring within the next N days.
     * Default is 7 days.
     */
    @Operation(summary = "Get expiring memberships", description = "Returns active memberships expiring within N days")
    @GetMapping("/expiring-memberships")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MembershipResponseDTO>> getExpiringMemberships(
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(dashboardService.getExpiringMemberships(days));
    }

    /**
     * Returns all overdue member payments.
     * Used by admin to follow up on unpaid dues.
     */
    @Operation(summary = "Get overdue payments", description = "Returns all member payments marked as overdue")
    @GetMapping("/overdue-payments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MemberPaymentResponseDTO>> getOverduePayments() {
        return ResponseEntity.ok(dashboardService.getOverduePayments());
    }
}