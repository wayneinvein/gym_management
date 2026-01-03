package com.gym.management.system.service.implememtationclasses;

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

    public MembershipPlan createPlan(MembershipPlan plan) {
        if (membershipPlanRepository.existsByName(plan.getName())) {
            throw new RuntimeException("Plan with same name already exists");
        }
        return membershipPlanRepository.save(plan);
    }

    public List<MembershipPlan> getAllPlans() {
        return membershipPlanRepository.findAll();
    }

    public MembershipPlan getPlan(Long id) {
        return membershipPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
    }

    public void deletePlan(Long id) {
        membershipPlanRepository.deleteById(id);
    }
}