package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.MemberRequest;
import com.gym.management.system.dto.response.MemberResponse;
import com.gym.management.system.entity.Members;

import java.util.List;

public interface MemberService {

    List<MemberResponse> getAllMembers();

    MemberResponse getMemberById(Long id);

    MemberResponse addMember(MemberRequest memberRequest);

    MemberResponse updateMember(Long id, MemberRequest memberRequest);

    void deleteMember(Long id);

    MemberResponse assignTrainer(Long memberId, Long trainerId);
}
