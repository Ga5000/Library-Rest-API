package com.ga5000.library.services;

import com.ga5000.library.model.Member;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.*;

public class VerificationService {

    private final JavaMailSender mailSender;
    private final ConcurrentHashMap<Long, String> verificationCodes = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public VerificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(Member member) {
        String verificationCode = generateVerificationCode();

        verificationCodes.put(member.getMemberId(), verificationCode);

        scheduler.schedule(() -> verificationCodes.remove(member.getMemberId()), 30, TimeUnit.MINUTES);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());
        message.setSubject("Password Change Verification Code");
        message.setText("Your verification code is: " + verificationCode + "\nThe code will expire in 30 minutes");
        mailSender.send(message);
    }

    public boolean verifyCode(Long memberId, String code) {
        String storedCode = verificationCodes.get(memberId);
        return storedCode != null && storedCode.equals(code);
    }

    public void invalidateCode(Long memberId) {
        verificationCodes.remove(memberId);
    }

    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }
}
