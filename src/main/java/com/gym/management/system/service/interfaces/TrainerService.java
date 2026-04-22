package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.TrainerRequestDTO;
import com.gym.management.system.dto.response.TrainerResponseDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;

import java.util.List;

/**
 * Service interface for managing trainers.
 *
 * Provides operations for CRUD on trainers and fetching assigned members.
 */
public interface TrainerService {

    // Get list of all trainers
    List<TrainerResponseDTO> getAllTrainers();

    // Get trainer details by ID
    TrainerResponseDTO getTrainerById(Long id);

    // Create a new trainer
    TrainerResponseDTO addTrainer(TrainerRequestDTO trainer);

    // Update existing trainer details
    TrainerResponseDTO updateTrainer(Long id, TrainerRequestDTO trainer);

    // Delete trainer by ID
    void deleteTrainer(Long id);

    // Get all members assigned to a specific trainer
    List<MemberResponseDTO> getMembersByTrainer(Long trainerId);

    // Update trainer active status
    TrainerResponseDTO updateTrainerStatus(Long id, boolean active);

    // Get profile of currently logged-in trainer
    TrainerResponseDTO getMyProfile(String username);
}