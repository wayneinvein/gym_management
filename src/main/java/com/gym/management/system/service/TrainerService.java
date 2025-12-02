package com.gym.management.system.service;

import com.gym.management.system.entity.Trainers;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TrainerService {

    public List<Trainers> getAllTrainers();

    public Trainers getTrainerById(Long id);

    public Trainers addTrainer(Trainers trainer);

    public Trainers updateTrainer(Long id, Trainers trainer);

    public void deleteTrainer(Long id);
}