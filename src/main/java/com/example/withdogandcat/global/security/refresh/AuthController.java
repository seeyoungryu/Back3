package com.example.withdogandcat.global.security.refresh;

import com.example.withdogandcat.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/reissue")
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader("Refresh-Token");
        TokenDto tokenData = authService.reissueToken(refreshToken);

        // 토큰을 응답 헤더에 추가
        jwtUtil.addTokensToHeaders(tokenData.getAccessToken(), tokenData.getRefreshToken(), response);

        // HTTP 상태 코드만 반환
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
