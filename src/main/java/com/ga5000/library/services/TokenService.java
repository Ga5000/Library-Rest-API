package com.ga5000.library.services;

import com.ga5000.library.model.Member;


public interface TokenService {
    String generateToken(Member member);
    String extractUsername(String token);
}
