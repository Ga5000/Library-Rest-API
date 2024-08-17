package com.ga5000.library.controllers;

import com.ga5000.library.dtos.Auth.AuthDTO;
import com.ga5000.library.dtos.Auth.AuthResponse;
import com.ga5000.library.dtos.Member.CreateMemberDTO;
import com.ga5000.library.model.Member;
import com.ga5000.library.services.AuthServiceImpl;
import com.ga5000.library.services.MemberService;
import com.ga5000.library.services.TokenServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

    private final AuthServiceImpl authServiceImpl;
    private final TokenServiceImpl tokenServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    public AuthControllerImpl(AuthServiceImpl authServiceImpl, TokenServiceImpl tokenServiceImpl, PasswordEncoder passwordEncoder, MemberService memberService) {
        this.authServiceImpl = authServiceImpl;
        this.tokenServiceImpl = tokenServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.memberService = memberService;
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid CreateMemberDTO request) {
        try{
            UserDetails existingUser = authServiceImpl.loadUserByUsername(request.username());
            if (existingUser != null) {
                return ResponseEntity.badRequest().body("Username already exists");
            }
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username wasn't found");
        }

        Member newMember = new Member();
        BeanUtils.copyProperties(request,newMember);
        newMember.setPassword(passwordEncoder.encode(request.password()));

        CreateMemberDTO createMemberDTO = new CreateMemberDTO(
                newMember.getUsername(),
                newMember.getEmail(),
                newMember.getPassword(),
                newMember.getPhoneNumber()
        );

        memberService.createMember(createMemberDTO);

        return ResponseEntity.ok("User registered successfully");
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthDTO request) {
        UserDetails userDetails = authServiceImpl.loadUserByUsername(request.username());
        if (userDetails == null || !passwordEncoder.matches(request.password(), ((Member) userDetails).getPassword())) {
            return ResponseEntity.badRequest().body(new AuthResponse("Invalid username or password"));
        }

        String token = tokenServiceImpl.generateToken((Member) userDetails);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
