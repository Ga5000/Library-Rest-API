package com.ga5000.library.controllers;

import com.ga5000.library.dtos.Member.AllMembersDTO;
import com.ga5000.library.dtos.Member.MemberDTO;
import com.ga5000.library.dtos.Member.UpdateMemberDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MemberController {
    ResponseEntity<Void> deleteMember(Long memberId, Authentication authentication);
    ResponseEntity<MemberDTO> getMemberDetails(Long memberId, Authentication authentication);
    ResponseEntity<MemberDTO> updateMember(Long memberId, Authentication authentication, UpdateMemberDTO updateMemberDTO);
    ResponseEntity<List<AllMembersDTO>> getAllMembers();
    ResponseEntity<Object> renewMemberShip(Long memberId, Authentication authentication);
    ResponseEntity<MemberDTO> getMemberByEmail(String email);
    ResponseEntity<MemberDTO> getMemberByPhoneNumber(String phoneNumber);
}
