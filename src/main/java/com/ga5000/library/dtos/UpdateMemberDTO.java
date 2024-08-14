package com.ga5000.library.dtos;

import com.ga5000.library.secutity.UserRole;

public record UpdateMemberDTO(String username, String email, String phoneNumber) {}
