package com.example.withdogandcat.domain.user;

import com.example.withdogandcat.domain.user.dto.SignupRequestDto;
import com.example.withdogandcat.global.email.EmailRequestDto;
import com.example.withdogandcat.global.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<Void> requestEmailVerification(@RequestBody EmailRequestDto requestDto) {
        emailService.sendVerificationEmail(requestDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> registerAccount(@RequestBody SignupRequestDto requestDto) {
        userService.registerNewAccount(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
