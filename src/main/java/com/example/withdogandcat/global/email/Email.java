package com.example.withdogandcat.global.email;

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

    private String verificationCode;
    private LocalDateTime expiryDate;
    private boolean registrationComplete;



    @Column(nullable = false)
    private boolean emailVerified = false;

    public Email(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.expiryDate = LocalDateTime.now().plusMinutes(5);
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isEmailVerified() {
        return emailVerified; // 필드 값 반환
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate; // 필드 값 반환
    }

    public boolean isRegistrationComplete() {
        return registrationComplete; // 필드 값 반환
    }
}
