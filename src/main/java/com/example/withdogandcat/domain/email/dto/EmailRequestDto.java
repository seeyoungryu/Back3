package com.example.withdogandcat.domain.email.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailRequestDto {

    private String email;
    private String verificationCode;

}
