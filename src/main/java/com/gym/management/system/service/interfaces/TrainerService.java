package com.gym.management.system.service.interfaces;

import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.Trainers;

import java.util.List;

public interface TrainerService {

    public List<Trainers> getAllTrainers();

    public Trainers getTrainerById(Long id);

    public Trainers addTrainer(Trainers trainer);

    public Trainers updateTrainer(Long id, Trainers trainer);

    public void deleteTrainer(Long id);

    public List<Members> getMembersByTrainer(Long trainerId);
}