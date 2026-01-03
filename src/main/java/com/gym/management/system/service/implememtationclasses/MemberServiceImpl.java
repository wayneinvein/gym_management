package com.gym.management.system.service.implememtationclasses;

import com.gym.management.system.dto.mapper.MemberMapper;
import com.gym.management.system.dto.request.MemberRequest;
import com.gym.management.system.dto.response.MemberResponse;
import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.Trainers;
import com.gym.management.system.exception.MemberNotFoundException;
import com.gym.management.system.exception.TrainerNotFoundException;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.TrainerRepository;
import com.gym.management.system.service.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final MemberMapper memberMapper;

    @Override
    public List<MemberResponse> getAllMembers() {
        List<Members> members = memberRepository.findAll();
        if (members.isEmpty()) {
            throw new MemberNotFoundException("Members not created yet!!");
        }

        //converting member(entity) object to memberResponse(dto) object
        return members.stream()
                .map(memberMapper::toResponse)
                .toList();
    }

    @Override
    public MemberResponse getMemberById(Long id) {
        Members member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));

        return memberMapper.toResponse(member);
    }

    @Override
    public MemberResponse addMember(MemberRequest memberRequest) {
        Members member = memberMapper.toEntity(memberRequest);

        Members saved = memberRepository.save(member);

        return memberMapper.toResponse(saved);
    }

    @Override
    public MemberResponse updateMember(Long id, MemberRequest memberRequest){

        Optional<Members> existing = memberRepository.findById(id);

        if(existing.isPresent()){
            Members m = existing.get();
            m.setMemberName(memberRequest.getMemberName());
            m.setMemberGender(memberRequest.getMemberGender());
            m.setPhoneNumber(memberRequest.getMemberPhoneNumber());

            Members updated = memberRepository.save(m);

            return memberMapper.toResponse(updated);
        }
        throw new MemberNotFoundException("Member not found with id:" + id);
    }

    @Override
    public void deleteMember(Long id){
        Optional<Members> existing = memberRepository.findById(id);
        if(existing.isPresent()) {
            memberRepository.deleteById(id);
        }else{
            throw new MemberNotFoundException("Member not found with id:" + id);
        }
    }

    @Override
    public MemberResponse assignTrainer(Long memberId, Long trainerId) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        Trainers trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with id: " + trainerId));

        member.setTrainer(trainer);
        memberRepository.save(member);

        Members updated = memberRepository.save(member);

        return memberMapper.toResponse(updated);
    }
}
