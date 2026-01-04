package com.gym.management.system.service.implememtationclasses;

import com.gym.management.system.dto.mapper.MemberDtoMapper;
import com.gym.management.system.dto.request.MemberRequestDto;
import com.gym.management.system.dto.response.MemberResponseDto;
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
    private final MemberDtoMapper memberDtoMapper;

    @Override
    public List<MemberResponseDto> getAllMembers() {
        List<Members> members = memberRepository.findAll();
        if (members.isEmpty()) {
            throw new MemberNotFoundException("Members not created yet!!");
        }

        //converting member(entity) object to memberResponse(dto) object
        return members.stream()
                .map(memberDtoMapper::toResponse)
                .toList();
    }

    @Override
    public MemberResponseDto getMemberById(Long id) {
        Members member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));

        return memberDtoMapper.toResponse(member);
    }

    @Override
    public MemberResponseDto addMember(MemberRequestDto memberRequestDto) {
        Members member = memberDtoMapper.toEntity(memberRequestDto);

        Members saved = memberRepository.save(member);

        return memberDtoMapper.toResponse(saved);
    }

    @Override
    public MemberResponseDto updateMember(Long id, MemberRequestDto memberRequestDto){

        Optional<Members> existing = memberRepository.findById(id);

        if(existing.isPresent()){
            Members m = existing.get();
            m.setMemberName(memberRequestDto.getMemberName());
            m.setMemberGender(memberRequestDto.getMemberGender());
            m.setPhoneNumber(memberRequestDto.getMemberPhoneNumber());

            Members updated = memberRepository.save(m);

            return memberDtoMapper.toResponse(updated);
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
    public MemberResponseDto assignTrainer(Long memberId, Long trainerId) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        Trainers trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with id: " + trainerId));

        member.setTrainer(trainer);
        memberRepository.save(member);

        Members updated = memberRepository.save(member);

        return memberDtoMapper.toResponse(updated);
    }
}
