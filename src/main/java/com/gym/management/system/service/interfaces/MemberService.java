package com.gym.management.system.service.interfaces;

import com.gym.management.system.entity.Members;

import java.util.List;

public interface MemberService {

    List<Members> getAllMembers();

    Members getMemberById(Long id);

    Members addMember(Members member);

    Members updateMember(Long id, Members member);

    void deleteMember(Long id);

     Members assignTrainer(Long memberId, Long trainerId);
}
