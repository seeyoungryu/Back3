package com.example.withdogandcat.global.security.refresh;


import com.example.withdogandcat.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @PostMapping("/reissue")
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader("Refresh-Token");
        TokenDto tokenData = authService.reissueToken(refreshToken);

        jwtUtil.addTokensToHeaders(tokenData.getAccessToken(), tokenData.getRefreshToken(), response);

        response.setStatus(HttpServletResponse.SC_OK);
    }

}
