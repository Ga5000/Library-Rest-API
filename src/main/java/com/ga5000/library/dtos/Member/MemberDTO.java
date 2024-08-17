package com.ga5000.library.dtos.Member;

import java.time.LocalDateTime;

public record MemberDTO(Long memberId, String username, String email, String phoneNumber, LocalDateTime membershipDate) {}
