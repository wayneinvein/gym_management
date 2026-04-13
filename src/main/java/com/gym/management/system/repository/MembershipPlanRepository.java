package com.gym.management.system.repository;

import com.gym.management.system.entity.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for MembershipPlan entity.
 * Handles database operations related to membership plans.
 */
@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {

    // Checks if a plan with the given name already exists
    boolean existsByName(String name);
}