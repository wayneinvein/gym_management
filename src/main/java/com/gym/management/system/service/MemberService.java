package com.gym.management.system.service;

import com.gym.management.system.entity.Members;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Members addMember(Members member);

    Members updateMember(Long id, Members member);

    void deleteMember(Long id);

    Optional<Members> getMemberById(Long id);

    List<Members> getAllMembers();
}
