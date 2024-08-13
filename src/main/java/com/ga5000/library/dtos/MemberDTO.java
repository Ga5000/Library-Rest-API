package com.ga5000.library.dtos;

import com.ga5000.library.secutity.UserRole;

import java.util.Date;

public record MemberDTO(Long memberId, String username, String email, String phoneNumber, Date membershipDate, UserRole role) {}
