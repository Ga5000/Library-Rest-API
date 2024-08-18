package com.ga5000.library.services;

import com.ga5000.library.dtos.Member.AllMembersDTO;
import com.ga5000.library.dtos.Member.CreateMemberDTO;
import com.ga5000.library.dtos.Member.MemberDTO;
import com.ga5000.library.dtos.Member.UpdateMemberDTO;

import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface MemberService {
    MemberDTO createMember(CreateMemberDTO member);
    MemberDTO getMemberById(Long id, String currentUsername) throws AccessDeniedException;
    MemberDTO updateMember(Long id, UpdateMemberDTO updatedMember, String currentUsername) throws AccessDeniedException;
    void deleteMember(Long id, String currentUsername) throws AccessDeniedException;

    UserDetails loadUserByUsername(String username);

    List<AllMembersDTO> getAllMembers();
    boolean isMemberActive(Long id);
    void renewMemberShip(Long id, String currentUsername) throws AccessDeniedException;

    MemberDTO findByEmail(String email);
    MemberDTO findByPhoneNumber(String phoneNumber);
}
