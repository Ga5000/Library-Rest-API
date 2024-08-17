package com.ga5000.library.config;

import com.ga5000.library.model.Member;
import com.ga5000.library.repositories.MemberRepository;
import com.ga5000.library.secutity.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if (memberRepository.findByUsername("admin") == null) {
                Member admin = new Member();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("a"));
                admin.setEmail("admin@example.com");
                admin.setPhoneNumber("1234567890");
                admin.setMembershipDate(LocalDateTime.now());
                admin.setRole(UserRole.ADMIN);
                memberRepository.save(admin);
            }
        };
    }
}
