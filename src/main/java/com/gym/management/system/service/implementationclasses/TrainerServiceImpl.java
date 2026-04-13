package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.TrainerDTOMapper;
import com.gym.management.system.dto.request.TrainerRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import com.gym.management.system.dto.response.TrainerResponseDTO;
import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.Trainers;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.TrainerRepository;
import com.gym.management.system.service.interfaces.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;
    private final TrainerDTOMapper trainerDTOMapper;

    // get all trainers
    @Override
    public List<TrainerResponseDTO> getAllTrainers() {
        List<Trainers> trainers = trainerRepository.findAll();
        return trainerDTOMapper.toResponse(trainers);
    }

    // get trainer by id
    @Override
    public TrainerResponseDTO getTrainerById(Long id) {
        Trainers trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainer with id " + id + " not found"));

        return trainerDTOMapper.toResponse(trainer);
    }

    // add trainer
    @Override
    public TrainerResponseDTO addTrainer(TrainerRequestDTO trainer) {
        Trainers newTrainer = trainerDTOMapper.toEntity(trainer);
        Trainers saved = trainerRepository.save(newTrainer);
        return trainerDTOMapper.toResponse(saved);
    }

    // update trainer
    @Override
    public TrainerResponseDTO updateTrainer(Long id, TrainerRequestDTO trainer) {

        Trainers existing = trainerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainer not found with id: " + id));

        existing.setTrainerName(trainer.getTrainerName());
        existing.setTrainerGender(trainer.getTrainerGender());
        existing.setPhoneNumber(trainer.getPhoneNumber());

        Trainers updated = trainerRepository.save(existing);

        return trainerDTOMapper.toResponse(updated);
    }

    // delete trainer
    @Override
    public void deleteTrainer(Long id) {
        Trainers existing = trainerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainer with id " + id + " doesn't exist"));

        trainerRepository.delete(existing);
    }

    // get members by trainer
    @Override
    public List<MemberResponseDTO> getMembersByTrainer(Long trainerId) {

        List<Members> members = memberRepository.findByTrainerTrainerId(trainerId);

        if (members.isEmpty()) {
            throw new NotFoundException("No members found for trainer id: " + trainerId);
        }

        // IMPORTANT: You need Member mapper here
        return members.stream()
                .map(member -> new MemberResponseDTO(
                        member.getMemberId(),
                        member.getMemberName(),
                        member.getMemberGender(),
                        member.getPhoneNumber()
                ))
                .toList();
    }
}