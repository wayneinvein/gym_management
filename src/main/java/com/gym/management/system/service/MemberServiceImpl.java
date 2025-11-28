package com.gym.management.system.service;

import com.gym.management.system.entity.Members;
import com.gym.management.system.exception.MemberNotFoundException;
import com.gym.management.system.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    //constructor dependency injection
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;  // Spring injects automatically
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
        memberRepository.deleteById(id);
    }

    @Override
    public Members getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));
    }

    @Override
    public List<Members> getAllMembers() {
        return memberRepository.findAll();
    }
}
