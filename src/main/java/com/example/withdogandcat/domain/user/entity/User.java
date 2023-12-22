package com.example.withdogandcat.domain.user.entity;

import com.example.withdogandcat.domain.user.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private boolean isActive = true;

    private boolean emailVerified;
    private LocalDateTime expiryDate;
    private boolean registrationComplete;

    @Builder
    private User(String email, String password, String phoneNumber, String nickname, UserRole role) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.role = role;
    }

    public static User of(SignupRequestDto requestDto, String password) {
        return User.builder()
                .email(requestDto.getEmail())
                .password(password)
                .phoneNumber(requestDto.getPhoneNumber())
                .nickname(requestDto.getNickname())
                .role(UserRole.USER)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    public void delete() {
        this.isActive = false;
    }
}
