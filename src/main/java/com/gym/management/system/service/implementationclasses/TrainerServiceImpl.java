package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MemberDTOMapper;
import com.gym.management.system.dto.mapper.TrainerDTOMapper;
import com.gym.management.system.dto.request.TrainerRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import com.gym.management.system.dto.response.TrainerResponseDTO;
import com.gym.management.system.entity.Member;
import com.gym.management.system.entity.Trainer;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.TrainerRepository;
import com.gym.management.system.service.interfaces.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing trainers.
 *
 * Handles CRUD operations for trainers and fetching assigned members.
 */
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;
    private final TrainerDTOMapper trainerDTOMapper;
    private final MemberDTOMapper memberDTOMapper;

    @Override
    public List<TrainerResponseDTO> getAllTrainers() {

        // Fetch all trainers from DB
        List<Trainer> trainers = trainerRepository.findAll();

        return trainerDTOMapper.toResponse(trainers);
    }

    @Override
    public TrainerResponseDTO getTrainerById(Long id) {

        // Fetch trainer or throw exception if not found
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainer with id " + id + " not found"));

        return trainerDTOMapper.toResponse(trainer);
    }

    @Override
    public TrainerResponseDTO addTrainer(TrainerRequestDTO trainer) {

        // Convert DTO to entity
        Trainer newTrainer = trainerDTOMapper.toEntity(trainer);

        // Save trainer in database
        Trainer saved = trainerRepository.save(newTrainer);

        return trainerDTOMapper.toResponse(saved);
    }

    @Override
    public TrainerResponseDTO updateTrainer(Long id, TrainerRequestDTO trainer) {

        // Fetch existing trainer
        Trainer existing = trainerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainer not found with id: " + id));

        // Update fields
        existing.setTrainerName(trainer.getTrainerName());
        existing.setTrainerGender(trainer.getTrainerGender());
        existing.setPhoneNumber(trainer.getPhoneNumber());

        // Save updated trainer
        Trainer updated = trainerRepository.save(existing);

        return trainerDTOMapper.toResponse(updated);
    }

    @Override
    public void deleteTrainer(Long id) {

        // Validate trainer exists before deletion
        Trainer existing = trainerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainer with id " + id + " doesn't exist"));

        trainerRepository.delete(existing);
    }

    @Override
    public List<MemberResponseDTO> getMembersByTrainer(Long trainerId) {

        // Verify trainer exists before fetching their members
        if (!trainerRepository.existsById(trainerId)) {
            throw new NotFoundException("Trainer not found with id: " + trainerId);
        }

        // Fetch all members assigned to this trainer
        List<Member> members = memberRepository.findByTrainerTrainerId(trainerId);

        // Empty list is valid — trainer may not have members assigned yet
        return members.stream()
                .map(memberDTOMapper::toResponse)
                .toList();
    }
}