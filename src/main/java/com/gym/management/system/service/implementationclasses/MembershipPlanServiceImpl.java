package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MembershipPlanDTOMapper;
import com.gym.management.system.dto.response.MembershipPlanResponseDTO;
import com.gym.management.system.entity.MembershipPlan;
import com.gym.management.system.repository.MembershipPlanRepository;
import com.gym.management.system.service.interfaces.MembershipPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipPlanDTOMapper membershipPlanDTOMapper;

    public MembershipPlanResponseDTO createPlan(MembershipPlan plan) {
        if (membershipPlanRepository.existsByName(plan.getName())) {
            throw new RuntimeException("Plan with same name already exists");
        }
        MembershipPlan savedPlan = membershipPlanRepository.save(plan);

        return membershipPlanDTOMapper.toResponse(savedPlan);
    }

    public List<MembershipPlanResponseDTO> getAllPlans() {

        List<MembershipPlan> plans = membershipPlanRepository.findAll();
        return membershipPlanDTOMapper.toResponse(plans);
    }

    public MembershipPlanResponseDTO getPlan(Long id) {

        MembershipPlan plan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan does not exist"));

        return membershipPlanDTOMapper.toResponse(plan);
    }

    public void deletePlan(Long id) {
        membershipPlanRepository.deleteById(id);
    }
}