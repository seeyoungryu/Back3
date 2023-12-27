package com.example.withdogandcat.global.security.refresh;

import com.example.withdogandcat.domain.user.entity.UserRole;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
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

    public TokenDto reissueToken(String refreshToken) throws BaseException {

        jwtUtil.validateToken(refreshToken);

        Authentication authentication = jwtUtil.getAuthentication(refreshToken);

        String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if (!redisRefreshToken.equals(refreshToken)) {
            throw new BaseException(BaseResponseStatus.NOT_EXIST_REFRESH_JWT);
        }

        TokenDto tokenDto = new TokenDto(
                jwtUtil.createAccessToken(authentication.getName(), UserRole.USER),
                jwtUtil.createRefreshToken(authentication.getName())
        );

        return tokenDto;
    }
}
