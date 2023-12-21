package com.example.withdogandcat.domain.email.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "email")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean emailVerified = false;

    private String verificationCode;
    private LocalDateTime expiryDate;
    private boolean registrationComplete;


    public Email(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.expiryDate = LocalDateTime.now().plusMinutes(5);
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public boolean isRegistrationComplete() {
        return registrationComplete;
    }

}
