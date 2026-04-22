package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MembershipPlanDTOMapper;
import com.gym.management.system.dto.request.MembershipPlanRequestDTO;
import com.gym.management.system.dto.response.MembershipPlanResponseDTO;
import com.gym.management.system.entity.MembershipPlan;
import com.gym.management.system.exception.AlreadyPresentException;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.MembershipPlanRepository;
import com.gym.management.system.service.interfaces.MembershipPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing membership plans.
 *
 * Handles creation, retrieval, update, deletion,
 * and active status toggling of membership plans.
 */
@Service
@RequiredArgsConstructor
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipPlanDTOMapper membershipPlanDTOMapper;

    /**
     * Creates a new membership plan.
     * Plan is always active on creation.
     * Throws AlreadyPresentException if plan name already exists.
     */
    @Override
    public MembershipPlanResponseDTO createPlan(MembershipPlanRequestDTO dto) {

        // Prevent duplicate plan names
        if (membershipPlanRepository.existsByName(dto.getName())) {
            throw new AlreadyPresentException("Plan with name '" + dto.getName() + "' already exists");
        }

        // Convert DTO to entity and set defaults
        MembershipPlan plan = membershipPlanDTOMapper.toEntity(dto);
        plan.setActive(true); // always active on creation

        return membershipPlanDTOMapper.toResponse(membershipPlanRepository.save(plan));
    }

    /**
     * Returns all membership plans.
     * Returns empty list if no plans exist — not an error.
     */
    @Override
    public List<MembershipPlanResponseDTO> getAllPlans() {
        return membershipPlanDTOMapper.toResponse(membershipPlanRepository.findAll());
    }

    /**
     * Fetches a single plan by ID.
     * Throws NotFoundException if plan does not exist.
     */
    @Override
    public MembershipPlanResponseDTO getPlanById(Long id) {
        MembershipPlan plan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plan not found with id: " + id));
        return membershipPlanDTOMapper.toResponse(plan);
    }

    /**
     * Updates an existing plan's details.
     * Throws NotFoundException if plan does not exist.
     */
    @Override
    public MembershipPlanResponseDTO updatePlan(Long id, MembershipPlanRequestDTO dto) {

        MembershipPlan plan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plan not found with id: " + id));

        // Update all editable fields
        plan.setName(dto.getName());
        plan.setDescription(dto.getDescription());
        plan.setDurationDays(dto.getDurationDays());
        plan.setPrice(dto.getPrice());

        return membershipPlanDTOMapper.toResponse(membershipPlanRepository.save(plan));
    }

    /**
     * Deletes a plan by ID.
     * Throws NotFoundException if plan does not exist.
     */
    @Override
    public void deletePlan(Long id) {

        MembershipPlan plan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plan not found with id: " + id));

        membershipPlanRepository.delete(plan);
    }

    /**
     * Toggles the active status of a plan.
     * Inactive plans cannot be assigned to new members.
     * Throws NotFoundException if plan does not exist.
     */
    @Override
    public MembershipPlanResponseDTO toggleStatus(Long id, boolean active) {

        MembershipPlan plan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Plan not found with id: " + id));

        plan.setActive(active);
        return membershipPlanDTOMapper.toResponse(membershipPlanRepository.save(plan));
    }
}