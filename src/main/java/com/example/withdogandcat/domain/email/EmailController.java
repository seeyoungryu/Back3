package com.example.withdogandcat.domain.email;

import com.example.mailtest.domain.email.dto.EmailRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailRequestDto requestDto) {
        boolean isVerified = emailService.verifyEmail(requestDto.getEmail(), requestDto.getVerificationCode());
        if (isVerified) {
            return ResponseEntity.ok("이메일이 성공적으로 인증되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증 실패했습니다.");
        }
    }

}
