package com.example.withdogandcat.domain.user;

import com.example.withdogandcat.domain.user.dto.SignupRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.domain.user.UserRepository;
import com.example.withdogandcat.domain.user.entity.UserRole;
import com.example.withdogandcat.global.email.Email;
import com.example.withdogandcat.global.email.EmailRepository;
import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.exception.ErrorCode;
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

    @Transactional
    public void registerNewAccount(SignupRequestDto requestDto) {
        checkIfEmailExist(requestDto.getEmail());

        Email email = emailRepository.findByEmailAndExpiryDateAfterAndEmailVerifiedTrue(
                        requestDto.getEmail(), LocalDateTime.now())
                .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));

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
    }

    private void checkIfEmailExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void deactivateAccount(Long userId, String inputPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
        userRepository.delete(user);
    }
}
