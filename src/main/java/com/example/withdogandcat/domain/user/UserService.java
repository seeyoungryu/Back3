package com.example.withdogandcat.domain.user;

import com.example.withdogandcat.domain.user.dto.SignupRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.domain.user.UserRepository;
import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerNewAccount(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        checkIfEmailExist(email);
        User user = User.from(requestDto, password);
        userRepository.save(user);
    }

    private void checkIfEmailExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
    }
}
