package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.response.MembershipPlanResponseDTO;
import com.gym.management.system.entity.MembershipPlan;

import java.util.List;

public interface MembershipPlanService {

    public MembershipPlanResponseDTO createPlan(MembershipPlan plan);
    public List<MembershipPlanResponseDTO> getAllPlans();
    public MembershipPlanResponseDTO getPlan(Long id);
    public void deletePlan(Long id);
}
