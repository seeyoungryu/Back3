package com.example.withdogandcat.domain.user;

import com.example.withdogandcat.domain.user.dto.DeactivateRequestDto;
import com.example.withdogandcat.domain.user.dto.SignupRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.email.EmailRequestDto;
import com.example.withdogandcat.global.email.EmailService;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/deactivate")
    public ResponseEntity<Void> deactivateAccount(@RequestBody DeactivateRequestDto request, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        userService.deactivateAccount(userDetails.getUser().getUserId(), request.getPassword());
        return ResponseEntity.noContent().build();
    }


}
