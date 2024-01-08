package com.example.withdogandcat.global.security.jwt;

import com.example.withdogandcat.domain.user.entity.UserRole;
import com.example.withdogandcat.global.exception.BaseException;
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
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    private final long ACCESS_EXPIRATION_TIME = 60 * 60 * 1000L;
    private final long REFRESH_EXPIRATION_TIME = 24 * 60 * 60 * 1000L;

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

        String jti = UUID.randomUUID().toString();
        String accessToken = Jwts.builder()
                .setSubject(username)
                .setId(jti)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, signatureAlgorithm)
                .compact();

        redisTemplate.opsForValue().set(
                username + "_access_jti",
                jti,
                ACCESS_EXPIRATION_TIME,
                TimeUnit.MILLISECONDS
        );

        return accessToken;
    }

    public String createRefreshToken(String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + REFRESH_EXPIRATION_TIME);

        String jti = UUID.randomUUID().toString();
        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setId(jti)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, signatureAlgorithm)
                .compact();

        redisTemplate.opsForValue().set(
                username + "_jti",
                jti,
                REFRESH_EXPIRATION_TIME,
                TimeUnit.MILLISECONDS
        );

        redisTemplate.opsForValue().set(
                username + "_refresh",
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

    public boolean validateToken(String token, boolean isRefreshToken) throws BaseException {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String username = claims.getSubject();
            String jti = claims.getId();

            if (!isRefreshToken) {
                String redisJti = redisTemplate.opsForValue().get(username + "_access_jti");
                if (redisJti == null || !jti.equals(redisJti)) {
                    throw new BaseException(BaseResponseStatus.INVALID_TOKEN);
                }
            }
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

    public String getJtiFromToken(String token) throws BaseException {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.getId();
        } catch (JwtException | IllegalArgumentException e) {
            throw new BaseException(BaseResponseStatus.INVALID_TOKEN);
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

    /**
     * 로그아웃
     * 레디스 데이터 삭제
     */
    public void logout(String username) {
        redisTemplate.delete(username + "_access_jti");
        redisTemplate.delete(username + "_jti");
        redisTemplate.delete(username + "_refresh");

        redisTemplate.delete("active_session:" + username);

        redisTemplate.delete("heartbeat:" + username);

        Set<String> sessionKeys = redisTemplate.keys("session:userEmail:*");
        for (String key : sessionKeys) {
            if (redisTemplate.opsForValue().get(key).equals(username)) {
                redisTemplate.delete(key);
            }
        }
    }

}
