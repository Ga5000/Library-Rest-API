package com.ga5000.library.secutity;

import com.ga5000.library.exceptions.MemberNotFoundException;
import com.ga5000.library.exceptions.TokenVerificationException;
import com.ga5000.library.repositories.MemberRepository;
import com.ga5000.library.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    public SecurityFilter(TokenService tokenService, MemberRepository memberRepository) {
        this.tokenService = tokenService;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            var username = tokenService.extractUsername(token);
            if (username != null) {
                UserDetails member = memberRepository.findByUsername(username);
                if (member != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new MemberNotFoundException("Member with username: " + username + " wasn't found");
                }
            } else {
                throw new TokenVerificationException("Invalid token");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
