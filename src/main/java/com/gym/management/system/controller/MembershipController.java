package com.gym.management.system.controller;

import com.gym.management.system.dto.request.MembershipRequestDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.dto.response.MembershipSummaryResponseDTO;
import com.gym.management.system.enums.MembershipStatus;
import com.gym.management.system.security.SecurityUtils;
import com.gym.management.system.service.interfaces.MembershipService;
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
 * REST controller for managing memberships.
 *
 * Access control:
 * - ADMIN  → full access
 * - MEMBER → can view their own membership history
 */
@Tag(name = "Membership APIs", description = "Operations related to member subscriptions")
@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;
    private final SecurityUtils securityUtils;

    /**
     * Creates a new membership for a member with a selected plan.
     * Cancels existing active membership if one exists.
     */
    @Operation(summary = "Create membership", description = "Assigns a plan to a member and creates subscription")
    @PostMapping("/member/{memberId}/plan/{planId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MembershipResponseDTO> createMembership(
            @PathVariable Long memberId,
            @PathVariable Long planId,
            @Valid @RequestBody MembershipRequestDTO dto) {
        return new ResponseEntity<>(
                membershipService.createMembership(memberId, planId, dto),
                HttpStatus.CREATED
        );
    }

    /**
     * Returns full membership history for a member.
     */
    @Operation(summary = "Get membership history", description = "Returns all past and current memberships for a member")
    @GetMapping("/member/{memberId}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MembershipResponseDTO>> getMembershipHistory(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(membershipService.getMembershipsByMemberId(memberId));
    }

    /**
     * Returns current active membership for a member.
     */
    @Operation(summary = "Get active membership")
    @GetMapping("/member/{memberId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ResponseEntity<MembershipResponseDTO> getActiveMembership(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(membershipService.getActiveMembership(memberId));
    }

    /**
     * Returns all memberships with pagination.
     */
    @Operation(summary = "Get all memberships")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MembershipResponseDTO>> getAllMemberships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "membershipId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(
                membershipService.getAllMemberships(page, size, sortBy, sortDir)
        );
    }

    /**
     * Returns memberships filtered by status.
     */
    @Operation(summary = "Get memberships by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MembershipResponseDTO>> getMembershipsByStatus(
            @PathVariable MembershipStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "membershipId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(
                membershipService.getMembershipsByStatus(status, page, size, sortBy, sortDir)
        );
    }

    /**
     * Cancels an active membership.
     */
    @Operation(summary = "Cancel membership")
    @PatchMapping("/{membershipId}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MembershipResponseDTO> cancelMembership(
            @PathVariable Long membershipId) {
        return ResponseEntity.ok(membershipService.cancelMembership(membershipId));
    }

    /**
     * Returns memberships expiring within the next N days.
     * Default is 7 days — used for dashboard expiry alerts.
     */
    @Operation(summary = "Get expiring memberships", description = "Returns active memberships expiring within N days")
    @GetMapping("/expiring")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MembershipResponseDTO>> getExpiringMemberships(
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(membershipService.getExpiringMemberships(days));
    }

    /**
     * Returns complete membership summary for logged-in member.
     * Includes days completed, days remaining, and payment status.
     */
    @Operation(summary = "Get my membership summary",
            description = "Returns membership details with day calculations for logged-in member")
    @GetMapping("/me/summary")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<MembershipSummaryResponseDTO> getMembershipSummary() {
        String username = securityUtils.getCurrentUsername();
        return ResponseEntity.ok(membershipService.getMembershipSummary(username));
    }
}