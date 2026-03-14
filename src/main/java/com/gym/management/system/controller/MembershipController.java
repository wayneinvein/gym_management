package com.gym.management.system.controller;

import com.gym.management.system.dto.request.MembershipRequestDto;
import com.gym.management.system.dto.response.MembershipResponseDto;
import com.gym.management.system.enums.MembershipStatus;
import com.gym.management.system.service.interfaces.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/create/{memberId}/{planId}")
    @PreAuthorize("hasRole('ADMIN')")
    public MembershipResponseDto createMembership(@PathVariable Long memberId, @PathVariable Long planId, @Valid @RequestBody MembershipRequestDto membershipRequestDto) {
        return membershipService.createMembership(memberId, planId, membershipRequestDto);
    }

    @PutMapping("/update/{membershipId}")
    @PreAuthorize("hasRole('ADMIN')")
    public MembershipResponseDto updateMembership(@PathVariable Long membershipId,
                                                  @Valid @RequestBody MembershipRequestDto membershipRequestDto) {
        return membershipService.updateMembership(membershipId, membershipRequestDto);
    }

    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public MembershipResponseDto getMembershipByMember(@PathVariable Long memberId) {
        return membershipService.getMembershipByMemberId(memberId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<MembershipResponseDto> getAllMemberships(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size,
                                                         @RequestParam(defaultValue = "membershipId") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String sortDir) {
        return membershipService.getAllMemberships(page, size, sortBy, sortDir);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<MembershipResponseDto> getByStatus(@PathVariable MembershipStatus status,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size,
                                                   @RequestParam(defaultValue = "membershipId") String sortBy,
                                                   @RequestParam(defaultValue = "asc") String sortDir) {
        return membershipService.getMembershipsByStatus(status, page, size, sortBy, sortDir);
    }

    @DeleteMapping("/delete/{membershipId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteMembership(@PathVariable Long membershipId) {
        return membershipService.deleteMembership(membershipId);
    }
}
