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

@Tag(name = "Member APIs", description = "Operations related to members") // swagger annotation
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor // used instead of @Autowired and manual constructor for injecting dependency
public class MemberController {

    //dependency of member service
    private final MemberService memberService;

    // Get all members
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

    // Get member by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> getMemberById(@PathVariable Long id) {
        MemberResponseDTO memberResponseDto = memberService.getMemberById(id); // throws MemberNotFoundException if not found
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    // Create a new member
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> addMember(@Valid @RequestBody MemberRequestDTO memberRequestDto) {
        MemberResponseDTO memberResponseDto = memberService.addMember(memberRequestDto);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.CREATED); // 201 Created
    }

    // Update existing member
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> updateMember(@PathVariable Long id, @Valid @RequestBody MemberRequestDTO memberRequestDto) {
        MemberResponseDTO memberResponseDto = memberService.updateMember(id, memberRequestDto); // throws MemberNotFoundException if not found
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    // delete member
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("Member deleted successfully with id: " + id);
    }

    //assigning a member to a trainer
    @PutMapping("/member/{memberId}/trainer/{trainerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDTO> assignTrainer(@PathVariable Long memberId, @PathVariable Long trainerId) {
        MemberResponseDTO memberResponseDto = memberService.assignTrainer(memberId, trainerId);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Get logged-in member profile",
            description = "Fetches profile details of the currently authenticated member"
    )//swagger annotation
    @GetMapping("/profile")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<MemberResponseDTO> getMyProfile() {
        return ResponseEntity.ok(memberService.getMyProfile());
    }

}
