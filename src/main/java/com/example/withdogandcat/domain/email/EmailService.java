package com.example.withdogandcat.domain.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Async
    public void sendVerificationEmail(String userEmail) {
        // 기존 이메일 레코드 찾기 및 삭제
        emailRepository.findByEmail(userEmail).ifPresent(emailRepository::delete);

        // 새 인증 코드 생성
        String verificationCode = generateVerificationCode();
        String content = "인증 코드 입니다. 제한시간 5분 ( 대소문자 구분 필수) : " + verificationCode;

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(userEmail);
            helper.setSubject("Email 인증 코드 입니다.");
            helper.setText(content, true);

            emailSender.send(message);
            logger.info("Verification email sent to: {}", userEmail);
        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}", userEmail, e);
        }

        // 새 이메일 레코드 추가
        Email email = new Email(userEmail, verificationCode);
        emailRepository.save(email);
    }


    private String generateVerificationCode() {
        int length = 6;
        String allowedChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%&";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return sb.toString();
    }

    public boolean verifyEmail(String userEmail, String verificationCode) {
        return emailRepository.findByEmail(userEmail)
                .map(email -> {
                    if (email.getVerificationCode().equals(verificationCode) && email.getExpiryDate().isAfter(LocalDateTime.now())) {
                        email.setEmailVerified(true);
                        emailRepository.save(email);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }
}
