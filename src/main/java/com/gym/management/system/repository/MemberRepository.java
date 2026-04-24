package com.gym.management.system.repository;

import com.gym.management.system.entity.Member;
import com.gym.management.system.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Members entity.
 * Provides database operations for gym members.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // Fetch all members assigned to a specific trainer
    List<Member> findByTrainerTrainerId(Long trainerId);

    // Fetch member based on username of associated user account
    Optional<Member> findByUserUsername(String username);

    // Check if a member with given phone number already exists
    boolean existsByPhoneNumber(String phoneNumber);

    // Count members by status
    long countByStatus(MemberStatus status);

    // Count new members joined between two dates
    long countByJoinedDateBetween(LocalDate startDate, LocalDate endDate);
}