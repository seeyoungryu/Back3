package com.example.withdogandcat.global.security.refresh;

import com.example.withdogandcat.domain.user.entity.UserRole;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenDto reissueToken(String refreshToken) throws CustomException {
        // Refresh Token 검증
        jwtUtil.validateToken(refreshToken);

        // Access Token 에서 사용자 이름을 가져옴
        Authentication authentication = jwtUtil.getAuthentication(refreshToken);

        // Redis에서 저장된 Refresh Token 값을 가져옴
        String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if(!redisRefreshToken.equals(refreshToken)) {
            throw new BaseException(BaseResponseStatus.NOT_EXIST_REFRESH_JWT);
        }

        // 토큰 재발행
        TokenDto tokenDto = new TokenDto(
                jwtUtil.createAccessToken(authentication.getName(), UserRole.USER),
                jwtUtil.createRefreshToken(authentication.getName())
        );

        return tokenDto;
    }
}
