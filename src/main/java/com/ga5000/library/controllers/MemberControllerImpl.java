package com.ga5000.library.controllers;

import com.ga5000.library.dtos.Member.AllMembersDTO;
import com.ga5000.library.dtos.Member.MemberDTO;
import com.ga5000.library.dtos.Member.UpdateMemberDTO;
import com.ga5000.library.exceptions.MemberNotFoundException;
import com.ga5000.library.services.MemberServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberControllerImpl implements MemberController {
    private final MemberServiceImpl memberService;

    public MemberControllerImpl(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{memberId}")
    @Override
    public ResponseEntity<Void> deleteMember(@PathVariable("memberId") Long memberId, Authentication authentication) {
        String currentUsername = authentication.getName();
        try {
            memberService.deleteMember(memberId, currentUsername);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{memberId}")
    @Override
    public ResponseEntity<MemberDTO> getMemberDetails(@PathVariable("memberId") Long memberId, Authentication authentication) {
        String currentUsername = authentication.getName();
        try {
            MemberDTO memberDTO = memberService.getMemberById(memberId, currentUsername);
            return ResponseEntity.ok(memberDTO);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
            return ResponseEntity.ok(updatedMember);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<List<AllMembersDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{memberId}/renewMembership")
    @Override
    public ResponseEntity<Object> renewMemberShip(@PathVariable("memberId") Long memberId, Authentication authentication) {
        String currentUsername = authentication.getName();
        try {
            memberService.renewMemberShip(memberId, currentUsername);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/find/email/{email}")
    @Override
    public ResponseEntity<MemberDTO> getMemberByEmail(@PathVariable("email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findByEmail(email));
    }

    @GetMapping("/find/phone/{phoneNumber}")
    @Override
    public ResponseEntity<MemberDTO> getMemberByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findByPhoneNumber(phoneNumber));
    }
}
