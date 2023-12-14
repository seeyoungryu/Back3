package com.example.withdogandcat.global.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findByEmail(String email);
    Optional<Email> findByEmailAndExpiryDateAfterAndEmailVerifiedTrue(String email, LocalDateTime now);
}
