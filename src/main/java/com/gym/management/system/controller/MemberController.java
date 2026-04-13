package com.gym.management.system.controller;

import com.gym.management.system.dto.request.MemberRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import com.gym.management.system.service.interfaces.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member APIs", description = "Operations related to members")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // Get paginated & sorted list of members (ADMIN only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MemberResponseDTO>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "memberId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(
                memberService.getAllMembers(page, size, sortBy, sortDir)
        );
    }

    // Get member by ID (ADMIN only)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    // Create new member (ADMIN only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> addMember(
            @Valid @RequestBody MemberRequestDTO memberRequestDto) {

        return new ResponseEntity<>(
                memberService.addMember(memberRequestDto),
                HttpStatus.CREATED
        );
    }

    // Update existing member (ADMIN only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequestDTO memberRequestDto) {

        return ResponseEntity.ok(
                memberService.updateMember(id, memberRequestDto)
        );
    }

    // Delete member by ID (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("Member deleted successfully with id: " + id);
    }

    // Assign trainer to a member (ADMIN only)
    @PutMapping("/member/{memberId}/trainer/{trainerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> assignTrainer(
            @PathVariable Long memberId,
            @PathVariable Long trainerId) {

        return ResponseEntity.ok(
                memberService.assignTrainer(memberId, trainerId)
        );
    }

    // Get profile of logged-in member
    @Operation(
            summary = "Get logged-in member profile",
            description = "Fetches profile details of the currently authenticated member"
    )
    @GetMapping("/profile")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<MemberResponseDTO> getMyProfile() {
        return ResponseEntity.ok(memberService.getMyProfile());
    }
}