package com.example.withdogandcat.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final Long userId;
    private final String nickname;
    private final String email;

    @Builder
    private LoginResponseDto(Long userId, String nickname, String email) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
    }

    public static LoginResponseDto from(User user) {
        return LoginResponseDto.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }
}
