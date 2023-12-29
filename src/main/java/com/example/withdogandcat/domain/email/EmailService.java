package com.example.withdogandcat.domain.email;

import com.example.mailtest.domain.email.entity.Email;
import com.example.mailtest.domain.user.UserRepository;
import com.example.mailtest.global.exception.BaseException;
import com.example.mailtest.global.exception.BaseResponseStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * 이메일 인증코드 발송
     */
    @Async
    public void sendVerificationEmail(String userEmail) {
        if (userRepository.existsByEmail(userEmail)) {
            logger.error("Email already registered: {}", userEmail);
            throw new BaseException(BaseResponseStatus.EMAIL_ALREADY_EXISTS);
        }

        emailRepository.findByEmail(userEmail).ifPresent(emailRepository::delete);

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

        Email email = new Email(userEmail, verificationCode);
        emailRepository.save(email);
    }

    /**
     * 이메일 인증코드 생성
     */
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

    /**
     * 이메일 인증
     */
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

    /**
     * 인증되지 않은 이메일 삭제
     */
    @Transactional
    public void deleteUnverifiedEmails(LocalDateTime now) {
        List<Email> emails = emailRepository.findAll();
        for (Email email : emails) {
            if (email.isEmailVerified() && !email.isRegistrationComplete() && email.getExpiryDate().isBefore(now)) {
                emailRepository.delete(email);
            }
        }
    }
}
