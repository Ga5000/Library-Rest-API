package com.ga5000.library.dtos.Member;

import java.util.Date;

public record MemberDTO(Long memberId, String username, String email, String phoneNumber, Date membershipDate) {}
