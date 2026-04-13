package com.gym.management.system.controller;

import com.gym.management.system.dto.request.TrainerRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import com.gym.management.system.dto.response.TrainerResponseDTO;
import com.gym.management.system.service.interfaces.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles trainer management:
 * - CRUD operations
 * - Fetch members assigned to a trainer
 *
 * Access: ADMIN only
 */
@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    /**
     * Create a new trainer.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponseDTO> addTrainer(@RequestBody TrainerRequestDTO trainer) {
        return new ResponseEntity<>(
                trainerService.addTrainer(trainer),
                HttpStatus.CREATED
        );
    }

    /**
     * Fetch all trainers.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrainerResponseDTO>> getAllTrainers() {
        return ResponseEntity.ok(trainerService.getAllTrainers());
    }

    /**
     * Fetch trainer by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponseDTO> getTrainerById(@PathVariable Long id) {
        return ResponseEntity.ok(trainerService.getTrainerById(id));
    }

    /**
     * Update trainer details.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponseDTO> updateTrainer(
            @PathVariable Long id,
            @RequestBody TrainerRequestDTO trainer) {

        return ResponseEntity.ok(
                trainerService.updateTrainer(id, trainer)
        );
    }

    /**
     * Delete trainer by ID.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.ok("Trainer deleted successfully with id: " + id);
    }

    /**
     * Fetch members assigned to a specific trainer.
     */
    @GetMapping("/{trainerId}/members")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MemberResponseDTO>> getMembersByTrainer(@PathVariable Long trainerId) {
        return ResponseEntity.ok(trainerService.getMembersByTrainer(trainerId));
    }
}