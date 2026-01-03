package com.gym.management.system.controller;

import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.Trainers;
import com.gym.management.system.service.interfaces.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    //dependency
    private final TrainerService trainerService;

    @PostMapping
    public ResponseEntity<Trainers> addTrainer(@RequestBody Trainers trainer) {
        Trainers savedTrainer = trainerService.addTrainer(trainer);
        return new ResponseEntity<>(savedTrainer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Trainers>> getAllTrainers() {
        List<Trainers> trainer = trainerService.getAllTrainers();
        return new ResponseEntity<>(trainer, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trainers> getTrainerById(@PathVariable Long id) {
        Trainers trainer = trainerService.getTrainerById(id);
        return new ResponseEntity<>(trainer, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trainers> updateTrainer(@PathVariable Long id, @RequestBody Trainers trainer) {
        Trainers updatedTrainer = trainerService.updateTrainer(id, trainer);
        return new ResponseEntity<>(updatedTrainer, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return new ResponseEntity<>("trainer with id: " + id + " has been successfully deleted", HttpStatus.CREATED);
    }

    //find members assigned to a particular trainer
    @GetMapping("/{trainerId}/members")
    public List<Members> getMembersByTrainer(@PathVariable Long trainerId) {
        return trainerService.getMembersByTrainer(trainerId);
    }


}
