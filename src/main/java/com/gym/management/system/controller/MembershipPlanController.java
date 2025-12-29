package com.gym.management.system.controller;

import com.gym.management.system.entity.MembershipPlan;
import com.gym.management.system.service.MembershipPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class MembershipPlanController {

    @Autowired
    private final MembershipPlanService membershipPlanService;

    public MembershipPlanController(MembershipPlanService membershipPlanService) {
        this.membershipPlanService = membershipPlanService;
    }

    @PostMapping
    public MembershipPlan createPlan(@RequestBody MembershipPlan plan) {
        return membershipPlanService.createPlan(plan);
    }

    @GetMapping
    public List<MembershipPlan> getPlans() {
        return membershipPlanService.getAllPlans();
    }

    @GetMapping("/{id}")
    public MembershipPlan getPlan(@PathVariable Long id) {
        return membershipPlanService.getPlan(id);
    }

    @DeleteMapping("/{id}")
    public void deletePlan(@PathVariable Long id) {
        membershipPlanService.deletePlan(id);
    }
}
