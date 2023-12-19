package com.example.withdogandcat.domain.chat.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorDto {

    private String email;
    private String nickname;

    @Builder
    public CreatorDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
