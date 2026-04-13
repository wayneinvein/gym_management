package com.gym.management.system.controller;

import com.gym.management.system.dto.mapper.TrainerDTOMapper;
import com.gym.management.system.dto.request.TrainerRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import com.gym.management.system.dto.response.TrainerResponseDTO;
import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.Trainers;
import com.gym.management.system.service.interfaces.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    //dependency
    private final TrainerService trainerService;
    private final TrainerDTOMapper trainerRequestDTO;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponseDTO> addTrainer(@RequestBody TrainerRequestDTO trainer) {
        TrainerResponseDTO savedTrainer = trainerService.addTrainer(trainer);
        return new ResponseEntity<>(savedTrainer, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrainerResponseDTO>> getAllTrainers() {
        List<TrainerResponseDTO> trainer = trainerService.getAllTrainers();
        return new ResponseEntity<>(trainer, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponseDTO> getTrainerById(@PathVariable Long id) {
        TrainerResponseDTO trainer = trainerService.getTrainerById(id);
        return new ResponseEntity<>(trainer, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponseDTO> updateTrainer(@PathVariable Long id, @RequestBody TrainerRequestDTO trainer) {
        TrainerResponseDTO updatedTrainer = trainerService.updateTrainer(id, trainer);
        return new ResponseEntity<>(updatedTrainer, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return new ResponseEntity<>("trainer with id: " + id + " has been successfully deleted", HttpStatus.CREATED);
    }

    //find members assigned to a particular trainer
    @GetMapping("/{trainerId}/members")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MemberResponseDTO> getMembersByTrainer(@PathVariable Long trainerId) {
        return trainerService.getMembersByTrainer(trainerId);
    }


}
