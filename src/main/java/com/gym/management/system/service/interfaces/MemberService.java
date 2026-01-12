package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.MemberRequestDto;
import com.gym.management.system.dto.response.MemberResponseDto;
import org.springframework.data.domain.Page;

public interface MemberService {

    //for pagination
    Page<MemberResponseDto> getAllMembers(int page, int size, String sortBy, String sortDir);

    MemberResponseDto getMemberById(Long id);

    MemberResponseDto addMember(MemberRequestDto memberRequestDto);

    MemberResponseDto updateMember(Long id, MemberRequestDto memberRequestDto);

    void deleteMember(Long id);

    MemberResponseDto assignTrainer(Long memberId, Long trainerId);
}
