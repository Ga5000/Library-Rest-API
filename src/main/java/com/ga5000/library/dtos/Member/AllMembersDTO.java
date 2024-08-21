package com.ga5000.library.dtos.Member;

import com.ga5000.library.secutity.UserRole;

import java.util.Date;

public record AllMembersDTO(Long memberId, String username, String email, String phoneNumber, Date membershipDate, UserRole role) {
}
