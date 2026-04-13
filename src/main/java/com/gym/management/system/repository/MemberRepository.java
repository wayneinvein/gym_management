package com.gym.management.system.repository;

import com.gym.management.system.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Members entity.
 * Provides database operations for gym members.
 */
@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

    // Fetch all members assigned to a specific trainer
    List<Members> findByTrainerTrainerId(Long trainerId);

    // Fetch member based on username of associated user account
    Optional<Members> findByUserUsername(String username);
}