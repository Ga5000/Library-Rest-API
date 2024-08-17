package com.ga5000.library.controllers;


import com.ga5000.library.services.MemberServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/members")
public class MemberControllerImpl implements MemberController {
    private final MemberServiceImpl memberService;

    public MemberControllerImpl(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }


    @Override
    public ResponseEntity<Void> deleteMember(Long memberId, Authentication authentication) {
        String currentUsername = authentication.getName();
        try{
            memberService.deleteMember(memberId,currentUsername);
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
            return ResponseEntity.noContent().build();
    }
}
