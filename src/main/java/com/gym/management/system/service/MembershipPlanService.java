package com.gym.management.system.service;

import com.gym.management.system.entity.MembershipPlan;

import java.util.List;

public interface MembershipPlanService {

    public MembershipPlan createPlan(MembershipPlan plan);
    public List<MembershipPlan> getAllPlans();
    public MembershipPlan getPlan(Long id);
    public void deletePlan(Long id);
}
