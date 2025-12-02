package com.gym.management.system.controller;

import com.gym.management.system.entity.Trainers;
import com.gym.management.system.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    //dependency
    private final TrainerService trainerService;

    //constructor dependency injection
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
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

    @PostMapping
    public ResponseEntity<Trainers> addTrainer(@RequestBody Trainers trainer) {
        Trainers savedTrainer = trainerService.addTrainer(trainer);
        return new ResponseEntity<>(savedTrainer, HttpStatus.CREATED);
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

}
