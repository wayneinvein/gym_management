package com.gym.management.system.service;

import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.Trainers;
import com.gym.management.system.exception.MemberNotFoundException;
import com.gym.management.system.exception.TrainerNotFoundException;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;

    //constructor dependency injection
    public MemberServiceImpl(MemberRepository memberRepository, TrainerRepository trainerRepository) {
        this.memberRepository = memberRepository;  // Spring injects automatically
        this.trainerRepository = trainerRepository;
    }

    @Override
    public List<Members> getAllMembers() {
        List<Members> members = memberRepository.findAll();
        if (members.isEmpty()) {
            throw new MemberNotFoundException("Members not created yet!!");
        }
        return members;
    }

    @Override
    public Members getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));
    }

    @Override
    public Members addMember(Members member){
        return memberRepository.save(member);
    }

    @Override
    public Members updateMember(Long id, Members member){
        Optional<Members> existing = memberRepository.findById(id);
        if(existing.isPresent()){
            Members m = existing.get();
            m.setMemberName(member.getMemberName());
            m.setMemberGender(member.getMemberGender());
            return memberRepository.save(m);
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
    public String assignTrainer(Long memberId, Long trainerId) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        Trainers trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with id: " + trainerId));

        member.setTrainer(trainer);

        return "member with id: " + memberId + " is assigned to trainer with id: " + trainerId +" successfully";
    }
}
