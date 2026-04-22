package com.gym.management.system.controller;

import com.gym.management.system.dto.request.MembershipPlanRequestDTO;
import com.gym.management.system.dto.response.MembershipPlanResponseDTO;
import com.gym.management.system.service.interfaces.MembershipPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing membership plans.
 *
 * Access control:
 * - ADMIN  → full access to all endpoints
 * - TRAINER, MEMBER → can only view active plans
 */
@Tag(name = "Membership Plan APIs", description = "Operations related to membership plans")
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class MembershipPlanController {

    private final MembershipPlanService membershipPlanService;

    /**
     * Creates a new membership plan.
     * Plan is always active on creation.
     */
    @Operation(summary = "Create membership plan")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MembershipPlanResponseDTO> createPlan(
            @Valid @RequestBody MembershipPlanRequestDTO dto) {
        return new ResponseEntity<>(
                membershipPlanService.createPlan(dto),
                HttpStatus.CREATED
        );
    }

    /**
     * Returns all membership plans.
     * Accessible by all authenticated users.
     */
    @Operation(summary = "Get all plans")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
    public ResponseEntity<List<MembershipPlanResponseDTO>> getAllPlans() {
        return ResponseEntity.ok(membershipPlanService.getAllPlans());
    }

    /**
     * Returns a single plan by ID.
     */
    @Operation(summary = "Get plan by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
    public ResponseEntity<MembershipPlanResponseDTO> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(membershipPlanService.getPlanById(id));
    }

    /**
     * Updates an existing plan's details.
     */
    @Operation(summary = "Update plan")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MembershipPlanResponseDTO> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody MembershipPlanRequestDTO dto) {
        return ResponseEntity.ok(membershipPlanService.updatePlan(id, dto));
    }

    /**
     * Deletes a plan permanently.
     */
    @Operation(summary = "Delete plan")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePlan(@PathVariable Long id) {
        membershipPlanService.deletePlan(id);
        return ResponseEntity.ok("Plan deleted successfully with id: " + id);
    }

    /**
     * Toggles active status of a plan.
     * Inactive plans cannot be assigned to new members.
     */
    @Operation(summary = "Toggle plan status", description = "Set plan active status to true or false")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MembershipPlanResponseDTO> toggleStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(membershipPlanService.toggleStatus(id, active));
    }
}