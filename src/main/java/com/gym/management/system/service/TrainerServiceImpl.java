package com.gym.management.system.service;

import com.gym.management.system.entity.Trainers;
import com.gym.management.system.exception.TrainerNotFoundException;
import com.gym.management.system.repository.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService{

    private final TrainerRepository trainerRepository;

    //injecting dependency of trainer repository through constructor
    public TrainerServiceImpl(TrainerRepository trainerRepository){
        this.trainerRepository = trainerRepository;
    }

    //get all trainers
    @Override
    public List<Trainers> getAllTrainers(){
        return trainerRepository.findAll();
    }

    //get trainer by id
    @Override
    public Trainers getTrainerById(Long id) {
        return trainerRepository.findById(id).orElseThrow(() -> new TrainerNotFoundException("trainer with id "+ id + " not found!!"));
    }

    //add trainer
    @Override
    public Trainers addTrainer(Trainers trainer) {
        return trainerRepository.save(trainer);
    }

    //update trainer
    @Override
    public Trainers updateTrainer(Long id, Trainers trainer) {
        Optional<Trainers> existing = trainerRepository.findById(id);

        if(existing.isPresent()){
            Trainers obj = existing.get();
            obj.setTrainerName(trainer.getTrainerName());
            obj.setTrainerGender(trainer.getTrainerGender());
            return trainerRepository.save(obj);
        }
        throw new TrainerNotFoundException("trainer not found with id: " + id);
    }

    //delete trainer
    @Override
    public void deleteTrainer(Long id) {
        Optional<Trainers> existing = trainerRepository.findById(id);
        if(existing.isPresent()){
            trainerRepository.deleteById(id);
        }else {
            throw new TrainerNotFoundException("trainer with id " + id + " dosen't exist!!");
        }
    }
}