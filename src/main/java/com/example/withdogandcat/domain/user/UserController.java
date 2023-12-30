package com.example.withdogandcat.domain.user;

import com.example.withdogandcat.domain.email.dto.EmailRequestDto;
import com.example.withdogandcat.domain.email.EmailService;
import com.example.withdogandcat.domain.user.dto.DeleteRequestDto;
import com.example.withdogandcat.domain.user.dto.SignupRequestDto;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final EmailService emailService;
    private final UserDeletionService userDeletionService;

    @PostMapping("/email")
    public ResponseEntity<BaseResponse<Void>> requestEmailVerification(@Validated @RequestBody EmailRequestDto requestDto) {
        emailService.sendVerificationEmail(requestDto.getEmail());
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "인증 성공", null));
    }

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<Void>> registerAccount(@Validated @RequestBody SignupRequestDto requestDto) {
        userService.registerNewAccount(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(BaseResponseStatus.SUCCESS, "회원가입 성공", null));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse<Void>> deleteAccount(@RequestBody DeleteRequestDto request,
                                                            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        userDeletionService.deleteAccount(userDetails.getUser().getUserId(), request.getPassword());
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUsername();

        jwtUtil.logout(username);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그아웃 성공", null));
    }
}
