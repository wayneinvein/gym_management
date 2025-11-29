package com.gym.management.system.controller;

import com.gym.management.system.entity.Members;
import com.gym.management.system.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    //dependency of member service
    private final MemberService memberService;

    //injecting dependency using constructor
    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    // Get all members
    @GetMapping
    public ResponseEntity<List<Members>> getAllMembers() {
        List<Members> members = memberService.getAllMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    // Get member by ID
    @GetMapping("/{id}")
    public ResponseEntity<Members> getMemberById(@PathVariable Long id) {
        Members member = memberService.getMemberById(id); // throws MemberNotFoundException if not found
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    // Create a new member
    @PostMapping
    public ResponseEntity<Members> addMember(@RequestBody Members member) {
        Members savedMember = memberService.addMember(member);
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED); // 201 Created
    }

    // Update existing member
    @PutMapping("/{id}")
    public ResponseEntity<Members> updateMember(@PathVariable Long id, @RequestBody Members member) {
        Members updatedMember = memberService.updateMember(id, member); // throws MemberNotFoundException if not found
        return new ResponseEntity<>(updatedMember, HttpStatus.OK);
    }

    // delete member
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("Member deleted successfully with id: " + id);
    }
}
