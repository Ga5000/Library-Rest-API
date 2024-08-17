package com.ga5000.library.controllers;

import com.ga5000.library.dtos.Auth.AuthDTO;
import com.ga5000.library.dtos.Auth.AuthResponse;
import com.ga5000.library.dtos.Member.CreateMemberDTO;

import org.springframework.http.ResponseEntity;


public interface AuthController {
    ResponseEntity<String> register( CreateMemberDTO request);
    ResponseEntity<AuthResponse> login(AuthDTO request);
}
