package com.ga5000.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ga5000.library.secutity.UserRole;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

import java.util.List;

@Entity
@Table(name = "members")
public class Member implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, length = 80)
    private String username;

    @Column(nullable = false, length = 255)
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime membershipDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Member(){}


    public Member(Long memberId, String username, String password, String email,
                  String phoneNumber, LocalDateTime membershipDate, UserRole role,
                  List<Comment> comments, List<Transaction> transactions) {
        this.memberId = memberId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.membershipDate = membershipDate;
        this.role = role;
        this.comments = comments;
        this.transactions = transactions;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDateTime membershipDate) {
        this.membershipDate = membershipDate;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (this.role) {
            case ADMIN -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER"));
            case USER -> List.of(new SimpleGrantedAuthority("ROLE_USER"));
        };
    }

    @Override
    public boolean isAccountNonExpired() {
        return LocalDateTime.now().isBefore(membershipDate.plusMonths(6));
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
