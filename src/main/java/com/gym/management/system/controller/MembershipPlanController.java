package com.gym.management.system.controller;

import com.gym.management.system.dto.response.MembershipPlanResponseDTO;
import com.gym.management.system.entity.MembershipPlan;
import com.gym.management.system.service.interfaces.MembershipPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class MembershipPlanController {

    @Autowired
    private final MembershipPlanService membershipPlanService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public MembershipPlanResponseDTO createPlan(@RequestBody MembershipPlan plan) {
        return membershipPlanService.createPlan(plan);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<MembershipPlanResponseDTO> getPlans() {
        return membershipPlanService.getAllPlans();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MembershipPlanResponseDTO getPlan(@PathVariable Long id) {
        return membershipPlanService.getPlan(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePlan(@PathVariable Long id) {
        membershipPlanService.deletePlan(id);
    }
}
