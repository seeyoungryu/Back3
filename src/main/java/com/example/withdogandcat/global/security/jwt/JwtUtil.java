package com.example.withdogandcat.global.security.jwt;

import com.example.mailtest.domain.user.entity.UserRole;
import com.example.mailtest.global.exception.BaseException;
import com.example.mailtest.global.exception.BaseResponseStatus;
import com.example.mailtest.global.security.impl.UserDetailsServiceImpl;
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

    // HttpServletRequest 헤더 추출
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader(AUTHORIZATION_HEADER);
    }

    public boolean validateToken(String token) throws BaseException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid token: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            logger.error("Expired token: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported token: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        } catch (IllegalArgumentException e) {
            logger.error("Token not found: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.NOT_FOUND_TOKEN);
        }
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUserEmailFromToken(String token) {
        Claims claims = getUserInfoFromToken(token);
        return claims.getSubject();
    }

    public void addTokensToHeaders(String accessToken, String refreshToken, HttpServletResponse response) {
        response.setHeader(AUTHORIZATION_HEADER, accessToken);
        response.setHeader(REFRESH_TOKEN_HEADER, refreshToken);
    }
}
