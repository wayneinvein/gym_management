package com.gym.management.system.controller;

import com.gym.management.system.dto.request.TrainerRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import com.gym.management.system.dto.response.TrainerResponseDTO;
import com.gym.management.system.security.SecurityUtils;
import com.gym.management.system.service.interfaces.TrainerService;
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
 * REST controller for managing gym trainers.
 *
 * Access control:
 * - ADMIN   → full access to all endpoints
 * - TRAINER → can only view their own profile and assigned members
 */
@Tag(name = "Trainer APIs", description = "Operations related to gym trainers")
@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final SecurityUtils securityUtils;

    /**
     * Creates a new trainer and auto-creates their login account.
     * Default login: username = phone number, password = "Trainer@123"
     */
    @Operation(summary = "Add new trainer", description = "Creates trainer and auto-creates login account")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponseDTO> addTrainer(
            @Valid @RequestBody TrainerRequestDTO dto) {
        return new ResponseEntity<>(
                trainerService.addTrainer(dto),
                HttpStatus.CREATED
        );
    }

    /**
     * Returns list of all trainers.
     */
    @Operation(summary = "Get all trainers")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrainerResponseDTO>> getAllTrainers() {
        return ResponseEntity.ok(trainerService.getAllTrainers());
    }

    /**
     * Returns a single trainer by ID.
     */
    @Operation(summary = "Get trainer by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponseDTO> getTrainerById(@PathVariable Long id) {
        return ResponseEntity.ok(trainerService.getTrainerById(id));
    }

    /**
     * Updates trainer details.
     */
    @Operation(summary = "Update trainer details")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponseDTO> updateTrainer(
            @PathVariable Long id,
            @Valid @RequestBody TrainerRequestDTO dto) {
        return ResponseEntity.ok(trainerService.updateTrainer(id, dto));
    }

    /**
     * Deletes trainer and their login account permanently.
     */
    @Operation(summary = "Delete trainer")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.ok("Trainer deleted successfully with id: " + id);
    }

    /**
     * Activates or deactivates a trainer.
     * Accepts active as a boolean query param: true or false
     */
    @Operation(summary = "Update trainer status", description = "Set trainer active status to true or false")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponseDTO> updateTrainerStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(trainerService.updateTrainerStatus(id, active));
    }

    /**
     * Returns all members assigned to a specific trainer.
     * Accessible by admin and the trainer themselves.
     */
    @Operation(summary = "Get members assigned to trainer")
    @GetMapping("/{trainerId}/members")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<MemberResponseDTO>> getMembersByTrainer(
            @PathVariable Long trainerId) {
        return ResponseEntity.ok(trainerService.getMembersByTrainer(trainerId));
    }

    /**
     * Returns the profile of the currently logged-in trainer.
     */
    @Operation(summary = "Get my profile", description = "Returns profile of the currently logged-in trainer")
    @GetMapping("/profile")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<TrainerResponseDTO> getMyProfile() {
        String username = securityUtils.getCurrentUsername();
        return ResponseEntity.ok(trainerService.getMyProfile(username));
    }

    /**
     * Allows logged-in trainer to update their own profile.
     */
    @Operation(summary = "Update my profile", description = "Trainer updates their own profile details")
    @PutMapping("/me")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<TrainerResponseDTO> updateMyProfile(
            @Valid @RequestBody TrainerRequestDTO dto) {
        String username = securityUtils.getCurrentUsername();
        return ResponseEntity.ok(trainerService.updateMyProfile(username, dto));
    }
}