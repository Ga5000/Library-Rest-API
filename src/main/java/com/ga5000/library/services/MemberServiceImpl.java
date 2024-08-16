package com.ga5000.library.services;

import com.ga5000.library.dtos.Member.CreateMemberDTO;
import com.ga5000.library.dtos.Member.MemberDTO;
import com.ga5000.library.dtos.Member.UpdateMemberDTO;
import com.ga5000.library.exceptions.MemberNotFoundException;
import com.ga5000.library.model.Member;
import com.ga5000.library.repositories.MemberRepository;
import com.ga5000.library.secutity.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final VerificationService verificationService;

    public MemberServiceImpl(MemberRepository memberRepository, VerificationService verificationService) {
        this.memberRepository = memberRepository;
        this.verificationService = verificationService;
    }

    @Transactional
    @Override
    public MemberDTO createMember(CreateMemberDTO createMemberDTO) {
        Member member = new Member();
        BeanUtils.copyProperties(createMemberDTO, member);
        member.setMembershipDate(new Date());
        return saveAndConvertToDTO(member);
    }

    @Override
    public MemberDTO getMemberById(Long id) {
        return findByIdAndConvertToDTO(id, this::toMemberDTO);
    }

    @Transactional
    @Override
    public MemberDTO updateMember(Long id, UpdateMemberDTO updateMemberDTO) {
        Member member = findById(id);
        BeanUtils.copyProperties(updateMemberDTO, member);
        return saveAndConvertToDTO(member);
    }

    @Transactional
    @Override
    public void deleteMember(Long id) {
        Member member = findById(id);
        memberRepository.deleteById(member.getMemberId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public void changePassword(Long id, String newPassword, String verificationCode) {
        if (!verificationService.verifyCode(id, verificationCode)) {
            throw new IllegalArgumentException("Invalid or expired verification code.");
        }

        Member member = findById(id);
        member.setPassword(newPassword);
        memberRepository.save(member);
        verificationService.invalidateCode(id);
    }

    @Override
    public void requestPasswordChange(String email) {
        Member member = memberRepository.findByEmail(email);
        verificationService.sendVerificationCode(member);
    }

    @Override
    public void updateUserRole(Long id, UserRole newRole) {
        Member member = findById(id);
        member.setRole(newRole);
        memberRepository.save(member);
    }

    @Override
    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll(Sort.by(Sort.Order.asc("username")))
                .stream()
                .map(this::toMemberDTO)
                .toList();
    }

    @Override
    public boolean isMemberActive(Long id) {
        Member member = findById(id);
        return member.isAccountNonExpired();
    }

    @Transactional
    @Override
    public void renewMemberShip(Long id) {
        Member member = findById(id);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 6);
        member.setMembershipDate(calendar.getTime());
        memberRepository.save(member);
    }

    @Override
    public MemberDTO findByEmail(String email) {

        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new MemberNotFoundException("Member wasn't found with email: " + email);
        }
        return toMemberDTO(member);
    }

    @Override
    public MemberDTO findByPhoneNumber(String phoneNumber) {
        Member member =  memberRepository.findByPhoneNumber(phoneNumber);

        if (member == null) {
            throw new MemberNotFoundException("Member wasn't found with phoneNumber: " + phoneNumber);
        }
        return toMemberDTO(member);
    }

    private Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member wasn't found with id: "+id));
    }

    @Transactional
    private MemberDTO saveAndConvertToDTO(Member member) {
        Member savedMember = memberRepository.save(member);
        return toMemberDTO(savedMember);
    }

    private MemberDTO findByIdAndConvertToDTO(Long id, Function<Member, MemberDTO> converter) {
        Member member = findById(id);
        return converter.apply(member);
    }

    private MemberDTO toMemberDTO(Member member) {
        return new MemberDTO(
                member.getMemberId(),
                member.getUsername(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getMembershipDate()
        );
    }
}
