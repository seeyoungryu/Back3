package com.example.withdogandcat.domain.email;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CleanupService {

    private final EmailRepository emailRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupUnverifiedEmails() {
        LocalDateTime now = LocalDateTime.now();
        emailRepository.findAll().forEach(email -> {
            if (!email.isEmailVerified() && email.getExpiryDate().isBefore(now)) {
                emailRepository.delete(email);
            }
        });
    }

    @Scheduled(cron = "0 5 0 * * ?")
    public void cleanupIncompleteRegistrations() {
        LocalDateTime now = LocalDateTime.now();
        emailService.deleteUnverifiedEmails(now);
    }

}
