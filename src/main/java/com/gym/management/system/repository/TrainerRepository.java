package com.gym.management.system.repository;

import com.gym.management.system.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Trainers entity.
 * Handles database operations for trainer data.
 */
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    // Check if trainer with phone number already exists
    boolean existsByPhoneNumber(String phoneNumber);

    // Find trainer by their linked user account's username
    Optional<Trainer> findByUserUsername(String username);

    // Count active trainers
    long countByActiveTrue();
}