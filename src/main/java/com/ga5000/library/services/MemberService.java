package com.ga5000.library.services;

import com.ga5000.library.model.Member;
import com.ga5000.library.secutity.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface MemberService {
    Member createMember(Member member);
    Member getMemberById(Long id);
    Member updateMember(Long id, Member updatedMember);
    void deleteMember(Long id);

    UserDetails loadUserByUsername(String username);
    void changePassword(Long id, String newPassword);
    void updateUserRole(Long id, UserRole newRole);

    List<Member> getAllMembers();
    boolean isMemberActive(Long id);
}
