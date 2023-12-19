package com.example.withdogandcat.global.security.jwt;

import com.example.withdogandcat.domain.user.entity.UserRole;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    private final long ACCESS_EXPIRATION_TIME = 60 * 60 * 1000L; // 60분
    private final long REFRESH_EXPIRATION_TIME = 72 * 60 * 60 * 1000L; // 72시간

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final RedisTemplate<String, String> redisTemplate;
    private final UserDetailsServiceImpl userDetailsService;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(String username, UserRole role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + ACCESS_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String createRefreshToken(String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + REFRESH_EXPIRATION_TIME);

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, signatureAlgorithm)
                .compact();

        redisTemplate.opsForValue().set(
                username,
                refreshToken,
                REFRESH_EXPIRATION_TIME,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    public Authentication getAuthentication(String token) {
        String userPrincipal = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest req) {
        return req.getHeader(AUTHORIZATION_HEADER);
    }

    public BaseResponse<String> validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", null);
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_TOKEN, "로그인 성공", "Invalid token");
        } catch (ExpiredJwtException e) {
            return new BaseResponse<>(BaseResponseStatus.EXPIRED_TOKEN, "로그인 성공", "Expired token");
        } catch (UnsupportedJwtException e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_REFRESH_TOKEN, "로그인 성공", "Unsupported token");
        } catch (IllegalArgumentException e) {
            return new BaseResponse<>(BaseResponseStatus.NOT_FOUND_TOKEN, "로그인 성공", "Token not found");
        }
    }


    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public void addTokensToHeaders(String accessToken, String refreshToken, HttpServletResponse response) {
        response.setHeader(AUTHORIZATION_HEADER, accessToken);
        response.setHeader(REFRESH_TOKEN_HEADER, refreshToken);
    }
}
