package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MemberDTOMapper;
import com.gym.management.system.dto.mapper.TrainerDTOMapper;
import com.gym.management.system.dto.request.TrainerRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import com.gym.management.system.dto.response.TrainerResponseDTO;
import com.gym.management.system.entity.Trainer;
import com.gym.management.system.entity.User;
import com.gym.management.system.enums.UserRoles;
import com.gym.management.system.exception.AlreadyPresentException;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.TrainerRepository;
import com.gym.management.system.repository.UserRepository;
import com.gym.management.system.service.interfaces.TrainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service implementation for managing gym trainers.
 *
 * Handles CRUD operations, auto user account creation,
 * active/inactive status, and fetching assigned members.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final TrainerDTOMapper trainerDTOMapper;
    private final MemberDTOMapper memberDTOMapper;
    private final PasswordEncoder passwordEncoder;

    //environment variable stored in application.properties
    @Value("${default.trainer.password}")
    private String defaultTrainerPassword;

    /**
     * Returns list of all trainers.
     * Returns empty list if no trainers exist — not an error.
     */
    @Override
    public List<TrainerResponseDTO> getAllTrainers() {
        return trainerDTOMapper.toResponse(trainerRepository.findAll());
    }

    /**
     * Fetches a single trainer by ID.
     * Throws NotFoundException if trainer does not exist.
     */
    @Override
    public TrainerResponseDTO getTrainerById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainer not found with id: " + id));
        return trainerDTOMapper.toResponse(trainer);
    }

    /**
     * Creates a new trainer and automatically creates a login account.
     *
     * Auto-created login credentials:
     * - Username = phone number
     * - Password = "Trainer@123" (default, should be changed after first login)
     * - Role = TRAINER
     *
     * Throws AlreadyPresentException if phone number already exists.
     */
    @Override
    @Transactional // rolls back both user and trainer creation if anything fails
    public TrainerResponseDTO addTrainer(TrainerRequestDTO dto) {

        // Check if phone number is already registered
        if (trainerRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new AlreadyPresentException("Trainer with phone number "
                    + dto.getPhoneNumber() + " already exists");
        }

        // Auto create login account for the trainer
        // Username = phone number, Password = default "Trainer@123"
        User user = new User();
        user.setUsername(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(defaultTrainerPassword));
        user.setUserRole(UserRoles.TRAINER);
        User savedUser = userRepository.save(user);

        // Convert DTO to entity and set system-managed fields
        Trainer trainer = trainerDTOMapper.toEntity(dto);
        trainer.setUser(savedUser);
        trainer.setJoiningDate(LocalDate.now()); // always today
        trainer.setActive(true);                 // always active on creation

        return trainerDTOMapper.toResponse(trainerRepository.save(trainer));
    }

    /**
     * Updates an existing trainer's details.
     * Throws NotFoundException if trainer does not exist.
     */
    @Override
    public TrainerResponseDTO updateTrainer(Long id, TrainerRequestDTO dto) {

        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainer not found with id: " + id));

        // Update all editable fields
        trainer.setTrainerName(dto.getTrainerName());
        trainer.setTrainerGender(dto.getTrainerGender());
        trainer.setPhoneNumber(dto.getPhoneNumber());
        trainer.setEmail(dto.getEmail());
        trainer.setSpecialization(dto.getSpecialization());
        trainer.setSalary(dto.getSalary());

        return trainerDTOMapper.toResponse(trainerRepository.save(trainer));
    }

    /**
     * Deletes a trainer and their associated login account.
     * Throws NotFoundException if trainer does not exist.
     */
    @Override
    @Transactional // ensures both trainer and user are deleted together
    public void deleteTrainer(Long id) {

        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainer not found with id: " + id));

        // Delete linked user account as well
        if (trainer.getUser() != null) {
            userRepository.delete(trainer.getUser());
        }

        trainerRepository.delete(trainer);
    }

    /**
     * Updates the active status of a trainer.
     * Inactive trainers cannot be assigned new members.
     */
    @Override
    public TrainerResponseDTO updateTrainerStatus(Long id, boolean active) {

        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainer not found with id: " + id));

        trainer.setActive(active);
        return trainerDTOMapper.toResponse(trainerRepository.save(trainer));
    }

    /**
     * Returns all members assigned to a specific trainer.
     * Returns empty list if trainer has no members assigned yet.
     */
    @Override
    public List<MemberResponseDTO> getMembersByTrainer(Long trainerId) {

        // Verify trainer exists before fetching members
        if (!trainerRepository.existsById(trainerId)) {
            throw new NotFoundException("Trainer not found with id: " + trainerId);
        }

        // Empty list is valid — trainer may not have members yet
        return memberRepository.findByTrainerTrainerId(trainerId)
                .stream()
                .map(memberDTOMapper::toResponse)
                .toList();
    }

    /**
     * Returns the profile of the currently logged-in trainer.
     * Reads username from Spring Security context.
     */
    @Override
    public TrainerResponseDTO getMyProfile(String username) {
        Trainer trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer profile not found"));
        return trainerDTOMapper.toResponse(trainer);
    }

    /**
     * Allows logged-in trainer to update their own profile.
     * Trainer can only update their own details — not other trainers.
     */
    @Override
    public TrainerResponseDTO updateMyProfile(String username, TrainerRequestDTO dto) {

        log.info("Trainer profile update request for username: {}", username);

        Trainer trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer profile not found"));

        // Check if new phone number is already taken by another trainer
        if (!trainer.getPhoneNumber().equals(dto.getPhoneNumber()) &&
                trainerRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new AlreadyPresentException("Phone number already registered");
        }

        trainer.setTrainerName(dto.getTrainerName());
        trainer.setTrainerGender(dto.getTrainerGender());
        trainer.setPhoneNumber(dto.getPhoneNumber());
        trainer.setEmail(dto.getEmail());
        trainer.setSpecialization(dto.getSpecialization());

        // Trainer cannot update their own salary — admin controls this
        log.info("Trainer profile updated successfully for username: {}", username);
        return trainerDTOMapper.toResponse(trainerRepository.save(trainer));
    }
}