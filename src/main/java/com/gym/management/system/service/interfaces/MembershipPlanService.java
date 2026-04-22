package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.MembershipPlanRequestDTO;
import com.gym.management.system.dto.response.MembershipPlanResponseDTO;

import java.util.List;

/**
 * Service interface for managing membership plans.
 */
public interface MembershipPlanService {

    // Create a new membership plan
    MembershipPlanResponseDTO createPlan(MembershipPlanRequestDTO dto);

    // Get all membership plans
    List<MembershipPlanResponseDTO> getAllPlans();

    // Get a specific plan by ID
    MembershipPlanResponseDTO getPlanById(Long id);

    // Update an existing plan
    MembershipPlanResponseDTO updatePlan(Long id, MembershipPlanRequestDTO dto);

    // Delete a plan by ID
    void deletePlan(Long id);

    // Toggle plan active status
    MembershipPlanResponseDTO toggleStatus(Long id, boolean active);
}