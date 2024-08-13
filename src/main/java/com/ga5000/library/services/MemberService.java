package com.ga5000.library.services;

import com.ga5000.library.dtos.CreateMemberDTO;
import com.ga5000.library.dtos.MemberDTO;
import com.ga5000.library.dtos.UpdateMemberDTO;

import com.ga5000.library.secutity.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface MemberService {
    MemberDTO createMember(CreateMemberDTO member);
    MemberDTO getMemberById(Long id);
    MemberDTO updateMember(Long id, UpdateMemberDTO updatedMember);
    void deleteMember(Long id);

    UserDetails loadUserByUsername(String username);
    void changePassword(Long id, String newPassword);
    void updateUserRole(Long id, UserRole newRole);

    List<MemberDTO> getAllMembers();
    boolean isMemberActive(Long id);
    void renewMemberShip(Long id);

    MemberDTO findByEmail(String email);
    MemberDTO findByPhoneNumber(String phoneNumber);
}
