package com.ga5000.library.services;

import com.ga5000.library.model.Member;
import com.ga5000.library.repositories.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberServiceImpl memberService;

    public AuthService(MemberRepository memberRepository, MemberServiceImpl memberService) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return member;
    }
}
