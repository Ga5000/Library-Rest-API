package com.ga5000.library.dtos.Member;

import com.ga5000.library.secutity.UserRole;

import java.time.LocalDateTime;

public record AllMembersDTO(Long memberId, String username, String email, String phoneNumber, LocalDateTime membershipDate, UserRole role) {
}
