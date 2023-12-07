package com.example.withdogandcat.domain.user.dto;

import com.example.withdogandcat.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final Long id;
    private final String nickname;

    @Builder
    private LoginResponseDto(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public static LoginResponseDto from(User user) {
        return LoginResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .build();
    }
}
