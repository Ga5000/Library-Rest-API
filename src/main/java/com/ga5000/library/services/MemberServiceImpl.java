package com.ga5000.library.services;

import com.ga5000.library.exceptions.MemberNotFoundException;
import com.ga5000.library.model.Member;
import com.ga5000.library.repositories.MemberRepository;
import com.ga5000.library.secutity.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public Member createMember(Member member) {
        return memberRepository.save(member) ;
    }

    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Transactional
    @Override
    public Member updateMember(Long id, Member updatedMember) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
        BeanUtils.copyProperties(updatedMember,member);
        return memberRepository.save(member);
    }

    @Transactional
    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));

        memberRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
       return memberRepository.findByUsername(username);
    }

    @Override
    public void changePassword(Long id, String newPassword) {

    }

    @Override
    public void updateUserRole(Long id, UserRole newRole) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));

        member.setRole(newRole);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll(Sort.by(Sort.Order.asc("userName")));
    }


    @Override
    public boolean isMemberActive(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));

        return member.isAccountNonExpired();
    }
}
