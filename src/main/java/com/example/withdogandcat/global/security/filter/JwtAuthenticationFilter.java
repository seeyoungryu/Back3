package com.example.withdogandcat.global.security.filter;

import com.example.withdogandcat.domain.user.dto.LoginRequestDto;
import com.example.withdogandcat.domain.user.entity.UserRole;
import com.example.withdogandcat.global.common.ApiResponseDto;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("로그인 시도");

        try {
            LoginRequestDto requestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            log.info("Email: {}", requestDto.getEmail());

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );

        } catch (IOException | AuthenticationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException {

        log.info("로그인 성공 및 JWT 생성");

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        String email = userDetails.getUsername();
        UserRole role = userDetails.getUser().getRole();
        String nickname = userDetails.getUser().getNickname();

        String accessToken = jwtUtil.createAccessToken(email, role);
        String refreshToken = jwtUtil.createRefreshToken(email);

        // 헤더에 액세스 토큰과 리프레시 토큰 추가
        jwtUtil.addTokensToHeaders(accessToken, refreshToken, response);

        ApiResponseDto<String> successResponse = new ApiResponseDto<>("로그인 성공", nickname);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(successResponse));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // 인증 실패 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException {

        log.info("로그인 실패: {}", failed.getMessage());

        ApiResponseDto<String> errorResponse = new ApiResponseDto<>("로그인 실패", null);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
