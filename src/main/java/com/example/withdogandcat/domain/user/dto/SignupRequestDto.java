package com.example.withdogandcat.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequestDto {

    @NotBlank(message = "이메일은 비어있을 수 없습니다")
    @Email(message = "형식에 맞게 입력하세요")
    private String email;

    @NotBlank(message = "비밀번호는 비어있을 수 없습니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{4,12}$",
            message = "비밀번호는 4~12자리면서 최소 하나의 알파벳과 숫자를 포함해야 합니다")
    private String password;


    @NotBlank(message = "닉네임은 비어있을 수 없습니다")
    private String nickname;

    @NotBlank(message = "전화번호는 비어있을 수 없습니다")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "전화번호는 10~11자리의 숫자이어야 합니다")
    private String phoneNumber;

}
