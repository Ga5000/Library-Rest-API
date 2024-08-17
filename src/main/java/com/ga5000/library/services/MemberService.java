package com.ga5000.library.services;

import com.ga5000.library.dtos.Member.CreateMemberDTO;
import com.ga5000.library.dtos.Member.MemberDTO;
import com.ga5000.library.dtos.Member.UpdateMemberDTO;

import com.ga5000.library.secutity.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface MemberService {
    MemberDTO createMember(CreateMemberDTO member);
    MemberDTO getMemberById(Long id);
    MemberDTO updateMember(Long id, UpdateMemberDTO updatedMember);
    void deleteMember(Long id, String currentUsername) throws AccessDeniedException;

    UserDetails loadUserByUsername(String username);
    void updateUserRole(Long id, UserRole newRole);

    List<MemberDTO> getAllMembers();
    boolean isMemberActive(Long id);
    void renewMemberShip(Long id);

    MemberDTO findByEmail(String email);
    MemberDTO findByPhoneNumber(String phoneNumber);
}
