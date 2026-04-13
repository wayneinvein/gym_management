package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.response.MembershipPlanResponseDTO;
import com.gym.management.system.entity.MembershipPlan;

import java.util.List;

/**
 * Service interface for managing membership plans.
 *
 * Defines operations for creating, retrieving, and deleting plans.
 */
public interface MembershipPlanService {

    // Create a new membership plan
    MembershipPlanResponseDTO createPlan(MembershipPlan plan);

    // Retrieve all available membership plans
    List<MembershipPlanResponseDTO> getAllPlans();

    // Get a specific plan by ID
    MembershipPlanResponseDTO getPlan(Long id);

    // Delete a plan by ID
    void deletePlan(Long id);
}