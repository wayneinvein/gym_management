package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MembershipPlanDTOMapper;
import com.gym.management.system.dto.response.MembershipPlanResponseDTO;
import com.gym.management.system.entity.MembershipPlan;
import com.gym.management.system.repository.MembershipPlanRepository;
import com.gym.management.system.service.interfaces.MembershipPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing membership plans.
 *
 * Handles creation, retrieval, and deletion of plans.
 */
@Service
@RequiredArgsConstructor
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipPlanDTOMapper membershipPlanDTOMapper;

    @Override
    public MembershipPlanResponseDTO createPlan(MembershipPlan plan) {

        // Prevent duplicate plan creation based on name
        if (membershipPlanRepository.existsByName(plan.getName())) {
            throw new RuntimeException("Plan with same name already exists");
        }

        // Save plan in database
        MembershipPlan savedPlan = membershipPlanRepository.save(plan);

        return membershipPlanDTOMapper.toResponse(savedPlan);
    }

    @Override
    public List<MembershipPlanResponseDTO> getAllPlans() {

        // Fetch all plans from database
        List<MembershipPlan> plans = membershipPlanRepository.findAll();

        return membershipPlanDTOMapper.toResponse(plans);
    }

    @Override
    public MembershipPlanResponseDTO getPlan(Long id) {

        // Fetch plan by ID or throw exception if not found
        MembershipPlan plan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan does not exist"));

        return membershipPlanDTOMapper.toResponse(plan);
    }

    @Override
    public void deletePlan(Long id) {

        // Delete plan by ID
        membershipPlanRepository.deleteById(id);
    }
}