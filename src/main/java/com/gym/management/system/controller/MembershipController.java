package com.gym.management.system.controller;

import com.gym.management.system.dto.request.MembershipRequestDto;
import com.gym.management.system.dto.response.MembershipResponseDto;
import com.gym.management.system.enums.MembershipStatus;
import com.gym.management.system.service.interfaces.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/create/{memberId}/{planId}")
    public MembershipResponseDto createMembership(@PathVariable Long memberId, @PathVariable Long planId, @Valid @RequestBody MembershipRequestDto membershipRequestDto) {
        return membershipService.createMembership(memberId, planId, membershipRequestDto);
    }

    @PutMapping("/update/{membershipId}")
    public MembershipResponseDto updateMembership(@PathVariable Long membershipId,
                                                  @Valid @RequestBody MembershipRequestDto membershipRequestDto) {
        return membershipService.updateMembership(membershipId, membershipRequestDto);
    }

    @GetMapping("/member/{memberId}")
    public MembershipResponseDto getMembershipByMember(@PathVariable Long memberId) {
        return membershipService.getMembershipByMemberId(memberId);
    }

    @GetMapping("/all")
    public List<MembershipResponseDto> getAllMemberships() {
        return membershipService.getAllMemberships();
    }

    @GetMapping("/status/{status}")
    public List<MembershipResponseDto> getByStatus(@PathVariable MembershipStatus status) {
        return membershipService.getMembershipsByStatus(status);
    }

    @DeleteMapping("/delete/{membershipId}")
    public String deleteMembership(@PathVariable Long membershipId) {
        return membershipService.deleteMembership(membershipId);
    }
}
