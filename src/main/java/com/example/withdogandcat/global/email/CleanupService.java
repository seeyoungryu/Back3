package com.example.withdogandcat.global.email;

import com.example.withdogandcat.domain.user.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import com.example.withdogandcat.domain.user.UserService;


@Service
public class CleanupService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public CleanupService(EmailRepository emailRepository, UserRepository userRepository, UserService userService) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupUnverifiedEmails() {
        LocalDateTime now = LocalDateTime.now();
        emailRepository.findAll().forEach(email -> {
            if (!email.isEmailVerified() && email.getExpiryDate().isBefore(now)) {
                emailRepository.delete(email);
            }
        });
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupIncompleteRegistrations() {
        LocalDateTime now = LocalDateTime.now();
        userService.deleteUnverifiedEmails(now);
    }
}

