package com.gym.management.system.repository;

import com.gym.management.system.entity.Trainers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Trainers entity.
 * Handles database operations for trainer data.
 */
@Repository
public interface TrainerRepository extends JpaRepository<Trainers, Long> {
    // Currently no custom queries required
}