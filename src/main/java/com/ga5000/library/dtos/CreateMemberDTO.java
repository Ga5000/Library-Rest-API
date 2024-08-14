package com.ga5000.library.dtos;

import com.ga5000.library.secutity.UserRole;

public record CreateMemberDTO(String username, String email, String password, String phoneNumber, UserRole role) {


}
