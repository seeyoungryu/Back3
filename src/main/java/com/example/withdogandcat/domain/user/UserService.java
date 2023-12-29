package com.example.withdogandcat.domain.user;

import com.example.withdogandcat.domain.email.EmailRepository;
import com.example.withdogandcat.domain.email.entity.Email;
import com.example.withdogandcat.domain.user.dto.SignupRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.domain.user.entity.UserRole;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailRepository emailRepository;

    /**
     * 회원가입
     */
    @Transactional
    public BaseResponse<User> registerNewAccount(SignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            return new BaseResponse<>(BaseResponseStatus.EMAIL_ALREADY_EXISTS, "이미 가입된 이메일 주소입니다.", null);
        }

        Email email = emailRepository.findByEmailAndExpiryDateAfterAndEmailVerifiedTrue(
                        requestDto.getEmail(), LocalDateTime.now())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.EMAIL_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User newUser = User.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .phoneNumber(requestDto.getPhoneNumber())
                .nickname(requestDto.getNickname())
                .role(UserRole.USER)
                .build();

        userRepository.save(newUser);
        emailRepository.delete(email);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "회원가입 성공", newUser);
    }

}
