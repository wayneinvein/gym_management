package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.MemberRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import org.springframework.data.domain.Page;

public interface MemberService {

    //for pagination
    Page<MemberResponseDTO> getAllMembers(int page, int size, String sortBy, String sortDir);

    MemberResponseDTO getMemberById(Long id);

    MemberResponseDTO addMember(MemberRequestDTO memberRequestDto);

    MemberResponseDTO updateMember(Long id, MemberRequestDTO memberRequestDto);

    void deleteMember(Long id);

    MemberResponseDTO assignTrainer(Long memberId, Long trainerId);

    MemberResponseDTO getMyProfile();
}
