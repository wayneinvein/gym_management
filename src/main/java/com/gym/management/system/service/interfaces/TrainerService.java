package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.TrainerRequestDTO;
import com.gym.management.system.dto.response.TrainerResponseDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;

import java.util.List;

public interface TrainerService {

    List<TrainerResponseDTO> getAllTrainers();

    TrainerResponseDTO getTrainerById(Long id);

    TrainerResponseDTO addTrainer(TrainerRequestDTO trainer);

    TrainerResponseDTO updateTrainer(Long id, TrainerRequestDTO trainer);

    void deleteTrainer(Long id);

    List<MemberResponseDTO> getMembersByTrainer(Long trainerId);
}