package com.ga5000.library.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface MemberController {
    ResponseEntity<Void> deleteMember(Long memberId, Authentication authentication);
}
