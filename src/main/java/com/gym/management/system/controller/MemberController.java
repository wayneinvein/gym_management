package com.gym.management.system.controller;

import com.gym.management.system.dto.request.MemberRequest;
import com.gym.management.system.dto.response.MemberResponse;
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
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> memberResponse = memberService.getAllMembers();
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    // Get member by ID
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.getMemberById(id); // throws MemberNotFoundException if not found
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    // Create a new member
    @PostMapping
    public ResponseEntity<MemberResponse> addMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.addMember(memberRequest);
        return new ResponseEntity<>(memberResponse, HttpStatus.CREATED); // 201 Created
    }

    // Update existing member
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.updateMember(id, memberRequest); // throws MemberNotFoundException if not found
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    // delete member
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("Member deleted successfully with id: " + id);
    }

    //assigning a member to a trainer
    @PutMapping("/member/{memberId}/trainer/{trainerId}")
    public ResponseEntity<MemberResponse> assignTrainer(@PathVariable Long memberId, @PathVariable Long trainerId) {
        MemberResponse memberResponse  = memberService.assignTrainer(memberId, trainerId);
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

}
