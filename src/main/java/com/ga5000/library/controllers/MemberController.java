package com.ga5000.library.controllers;

import com.ga5000.library.dtos.Member.MemberDTO;
import com.ga5000.library.dtos.Member.UpdateMemberDTO;
import com.ga5000.library.secutity.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface MemberController {
    ResponseEntity<Void> deleteMember(Long memberId, Authentication authentication);
    ResponseEntity<MemberDTO> getMemberDetails(Long memberId, Authentication authentication);
    ResponseEntity<MemberDTO> updateMember(Long memberId, Authentication authentication, UpdateMemberDTO updateMemberDTO);
}
