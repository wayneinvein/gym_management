package com.gym.management.system.controller;

import com.gym.management.system.dto.request.MemberRequestDto;
import com.gym.management.system.dto.response.MemberResponseDto;
import com.gym.management.system.service.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor // used instead of @Autowired and manual constructor for injecting dependency
public class MemberController {

    //dependency of member service
    private final MemberService memberService;

    // Get all members
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        List<MemberResponseDto> memberResponseDto = memberService.getAllMembers();
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    // Get member by ID
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable Long id) {
        MemberResponseDto memberResponseDto = memberService.getMemberById(id); // throws MemberNotFoundException if not found
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    // Create a new member
    @PostMapping
    public ResponseEntity<MemberResponseDto> addMember(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.addMember(memberRequestDto);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.CREATED); // 201 Created
    }

    // Update existing member
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable Long id, @RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.updateMember(id, memberRequestDto); // throws MemberNotFoundException if not found
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    // delete member
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("Member deleted successfully with id: " + id);
    }

    //assigning a member to a trainer
    @PutMapping("/member/{memberId}/trainer/{trainerId}")
    public ResponseEntity<MemberResponseDto> assignTrainer(@PathVariable Long memberId, @PathVariable Long trainerId) {
        MemberResponseDto memberResponseDto = memberService.assignTrainer(memberId, trainerId);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

}
