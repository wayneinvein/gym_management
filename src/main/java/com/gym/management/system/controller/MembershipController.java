package com.gym.management.system.controller;

import com.gym.management.system.entity.Membership;
import com.gym.management.system.enums.MembershipStatus;
import com.gym.management.system.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/create/{memberId}/{planId}")
    public Membership createMembership(@PathVariable Long memberId, @PathVariable Long planId, @RequestBody Membership membership) {
        return membershipService.createMembership(memberId, planId, membership);
    }

    @PutMapping("/update/{membershipId}")
    public Membership updateMembership(@PathVariable Long membershipId,
                                       @RequestBody Membership membership) {
        return membershipService.updateMembership(membershipId, membership);
    }

    @GetMapping("/member/{memberId}")
    public Membership getMembershipByMember(@PathVariable Long memberId) {
        return membershipService.getMembershipByMemberId(memberId);
    }

    @GetMapping("/all")
    public List<Membership> getAllMemberships() {
        return membershipService.getAllMemberships();
    }

    @GetMapping("/status/{status}")
    public List<Membership> getByStatus(@PathVariable MembershipStatus status) {
        return membershipService.getMembershipsByStatus(status);
    }

    @DeleteMapping("/delete/{membershipId}")
    public String deleteMembership(@PathVariable Long membershipId) {
        return membershipService.deleteMembership(membershipId);
    }
}
