package com.example.withdogandcat.domain.user.dto;

import com.example.withdogandcat.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final Long userId;
    private final String nickname;

    @Builder
    private LoginResponseDto(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public static LoginResponseDto from(User user) {
        return LoginResponseDto.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .build();
    }
}
