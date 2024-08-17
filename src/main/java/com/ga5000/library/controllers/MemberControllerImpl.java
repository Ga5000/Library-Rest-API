package com.ga5000.library.controllers;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga5000.library.dtos.Member.MemberDTO;
import com.ga5000.library.dtos.Member.UpdateMemberDTO;
import com.ga5000.library.exceptions.MemberNotFoundException;
import com.ga5000.library.secutity.UserRole;
import com.ga5000.library.services.MemberServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/members")
public class MemberControllerImpl implements MemberController {
    private final MemberServiceImpl memberService;

    public MemberControllerImpl(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }

    @DeleteMapping("/{memberId}")
    @Override
    public ResponseEntity<Void> deleteMember(@PathVariable("memberId") Long memberId, Authentication authentication) {
        String currentUsername = authentication.getName();
        try {
            memberService.deleteMember(memberId, currentUsername);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/{memberId}")
    @Override
    public ResponseEntity<MemberDTO> getMemberDetails(@PathVariable("memberId") Long memberId, Authentication authentication) {
        String currentUsername = authentication.getName();
        try {
            MemberDTO memberDTO = memberService.getMemberById(memberId, currentUsername);
            return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{memberId}")
    @Override
    public ResponseEntity<MemberDTO> updateMember(@PathVariable("memberId") Long memberId, Authentication authentication,
                                                  @RequestBody @Valid UpdateMemberDTO updateMemberDTO) {
        String currentUsername = authentication.getName();
        try {

            MemberDTO updatedMember = memberService.updateMember(memberId, updateMemberDTO, currentUsername);
            return ResponseEntity.status(HttpStatus.OK).body(updatedMember);
        } catch (AccessDeniedException e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (MemberNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }





}
