package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.MemberRequestDto;
import com.gym.management.system.dto.response.MemberResponseDto;

import java.util.List;

public interface MemberService {

    List<MemberResponseDto> getAllMembers();

    MemberResponseDto getMemberById(Long id);

    MemberResponseDto addMember(MemberRequestDto memberRequestDto);

    MemberResponseDto updateMember(Long id, MemberRequestDto memberRequestDto);

    void deleteMember(Long id);

    MemberResponseDto assignTrainer(Long memberId, Long trainerId);
}
