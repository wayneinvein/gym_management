package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MemberDtoMapper;
import com.gym.management.system.dto.request.MemberRequestDto;
import com.gym.management.system.dto.response.MemberResponseDto;
import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.Trainers;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.TrainerRepository;
import com.gym.management.system.service.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final MemberDtoMapper memberDtoMapper;

    @Override
    public Page<MemberResponseDto> getAllMembers(int page, int size, String sortBy, String sortDir) {

        //sorting
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        //pagination
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Members> membersPage = memberRepository.findAll(pageable);

        if (membersPage.isEmpty()) {throw new NotFoundException("Members not found"); }

        return membersPage.map(memberDtoMapper::toResponse);
    }

    @Override
    public MemberResponseDto getMemberById(Long id) {
        Members member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + id));

        return memberDtoMapper.toResponse(member);
    }

    @Override
    public MemberResponseDto addMember(MemberRequestDto memberRequestDto) {

        //object of memberRequestDto converted into Member entity
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
            m.setPhoneNumber(memberRequestDto.getPhoneNumber());

            Members updated = memberRepository.save(m);

            return memberDtoMapper.toResponse(updated);
        }
        throw new NotFoundException("Member not found with id:" + id);
    }

    @Override
    public void deleteMember(Long id){
        Optional<Members> existing = memberRepository.findById(id);
        if(existing.isPresent()) {
            memberRepository.deleteById(id);
        }else{
            throw new NotFoundException("Member not found with id:" + id);
        }
    }

    @Override
    public MemberResponseDto assignTrainer(Long memberId, Long trainerId) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + memberId));

        Trainers trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new NotFoundException("Trainer not found with id: " + trainerId));

        member.setTrainer(trainer);
        memberRepository.save(member);

        Members updated = memberRepository.save(member);

        return memberDtoMapper.toResponse(updated);
    }
}
