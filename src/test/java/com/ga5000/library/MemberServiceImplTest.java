package com.ga5000.library;

import com.ga5000.library.dtos.Member.CreateMemberDTO;
import com.ga5000.library.dtos.Member.MemberDTO;
import com.ga5000.library.dtos.Member.UpdateMemberDTO;
import com.ga5000.library.exceptions.MemberNotFoundException;
import com.ga5000.library.model.Member;
import com.ga5000.library.repositories.MemberRepository;
import com.ga5000.library.services.MemberServiceImpl;
import com.ga5000.library.services.VerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private VerificationService verificationService;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMember_shouldCreateMemberSuccessfully() {
        CreateMemberDTO createMemberDTO = new CreateMemberDTO("username", "password", "email", "phone");
        Member member = new Member();
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDTO result = memberService.createMember(createMemberDTO);

        assertNotNull(result);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void getMemberById_shouldReturnMemberDTOWhenFound() {
        Member member = new Member();
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        MemberDTO result = memberService.getMemberById(1L);

        assertNotNull(result);
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    void getMemberById_shouldThrowExceptionWhenNotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.getMemberById(1L));
    }

    @Test
    void updateMember_shouldUpdateMemberSuccessfully() {
        Member member = new Member();
        UpdateMemberDTO updateMemberDTO = new UpdateMemberDTO("newUsername", "newEmail", "newPhone");

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDTO result = memberService.updateMember(1L, updateMemberDTO);

        assertNotNull(result);
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    void deleteMember_shouldDeleteMemberSuccessfully() {
        Member member = new Member();
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        member.setMemberId(1L);

        memberService.deleteMember(1L);

        verify(memberRepository, times(1)).deleteById(1L);
    }

    @Test
    void changePassword_shouldChangePasswordWhenCodeIsValid() {
        Member member = new Member();
        member.setPassword("oldPassword");

        when(verificationService.verifyCode(anyLong(), anyString())).thenReturn(true);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        memberService.changePassword(1L, "newPassword", "1234");

        verify(memberRepository, times(1)).save(member);
        verify(verificationService, times(1)).invalidateCode(1L);
        assertEquals("newPassword", member.getPassword());
    }

    @Test
    void changePassword_shouldThrowExceptionWhenCodeIsInvalid() {
        when(verificationService.verifyCode(anyLong(), anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> memberService.changePassword(1L, "newPassword", "1234"));
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void requestPasswordChange_shouldSendVerificationCode() {
        Member member = new Member();
        when(memberRepository.findByEmail(anyString())).thenReturn(member);

        memberService.requestPasswordChange("email");

        verify(verificationService, times(1)).sendVerificationCode(member);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetailsWhenFound() {
        UserDetails userDetails = mock(UserDetails.class);
        when(memberRepository.findByUsername(anyString())).thenReturn(userDetails);

        UserDetails result = memberService.loadUserByUsername("username");

        assertEquals(userDetails, result);
    }
}
