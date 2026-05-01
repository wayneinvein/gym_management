package com.gym.management.system.controller;

import com.gym.management.system.dto.request.MemberRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import com.gym.management.system.enums.MemberStatus;
import com.gym.management.system.service.interfaces.MemberService;
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
 * REST controller for managing gym members.
 *
 * Exposes endpoints for CRUD operations, trainer assignment,
 * status updates, and member self-service profile access.
 *
 * Access control:
 * - ADMIN  → full access to all endpoints
 * - TRAINER → can view assigned members
 * - MEMBER  → can only access their own profile
 */
@Tag(name = "Member APIs", description = "Operations related to gym members")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * Creates a new member and auto-creates their login account.
     * Default login: username = phone number, password = "Gym@123"
     */
    @Operation(summary = "Add new member", description = "Creates member and auto-creates login account")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> addMember(
            @Valid @RequestBody MemberRequestDTO memberRequestDto) {
        return new ResponseEntity<>(
                memberService.addMember(memberRequestDto),
                HttpStatus.CREATED
        );
    }

    /**
     * Returns paginated and sorted list of all members.
     * Supports pagination via page/size and sorting via sortBy/sortDir params.
     */
    @Operation(summary = "Get all members", description = "Returns paginated list of all gym members")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MemberResponseDTO>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "memberId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(
                memberService.getAllMembers(page, size, sortBy, sortDir)
        );
    }

    /**
     * Returns a single member by their ID.
     */
    @Operation(summary = "Get member by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    /**
     * Updates an existing member's details.
     * Status is updated separately via PATCH /status endpoint.
     */
    @Operation(summary = "Update member details")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequestDTO memberRequestDto) {
        return ResponseEntity.ok(
                memberService.updateMember(id, memberRequestDto)
        );
    }

    /**
     * Deletes a member and their associated login account permanently.
     */
    @Operation(summary = "Delete member")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("Member deleted successfully with id: " + id);
    }

    /**
     * Updates the status of a member.
     * Used to activate, deactivate, or suspend a member.
     * Accepts status as a query param: ACTIVE, INACTIVE, SUSPENDED
     */
    @Operation(summary = "Update member status", description = "Set member status to ACTIVE, INACTIVE or SUSPENDED")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> updateMemberStatus(
            @PathVariable Long id,
            @RequestParam MemberStatus status) {
        return ResponseEntity.ok(
                memberService.updateMemberStatus(id, status)
        );
    }

    /**
     * Assigns a trainer to a member.
     * Both member and trainer must exist.
     */
    @Operation(summary = "Assign trainer to member")
    @PutMapping("/{memberId}/trainer/{trainerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> assignTrainer(
            @PathVariable Long memberId,
            @PathVariable Long trainerId) {
        return ResponseEntity.ok(
                memberService.assignTrainer(memberId, trainerId)
        );
    }

    /**
     * Returns all members assigned to a specific trainer.
     * Accessible by admin and the trainer themselves.
     */
    @Operation(summary = "Get members by trainer")
    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<MemberResponseDTO>> getMembersByTrainer(
            @PathVariable Long trainerId) {
        return ResponseEntity.ok(
                memberService.getMembersByTrainer(trainerId)
        );
    }

    /**
     * Returns the profile of the currently logged-in member.
     * Member can only see their own profile — not other members.
     */
    @Operation(summary = "Get my profile", description = "Returns profile of the currently logged-in member")
    @GetMapping("/profile")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<MemberResponseDTO> getMyProfile() {
        return ResponseEntity.ok(memberService.getMyProfile());
    }
}