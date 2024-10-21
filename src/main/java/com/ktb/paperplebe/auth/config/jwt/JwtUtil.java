package com.ktb.paperplebe.auth.config.jwt;

import com.ktb.paperplebe.auth.dto.JwtClaimResponse;
import com.ktb.paperplebe.user.constant.UserRole;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
@Getter
public class JwtUtil {
    private static final String ROLE_CLAIM = "role";
    private static final String OAUTH_PROVIDER_CLAIM = "provider";
    private static final String USER_ID_CLAIM = "userId";

    public static final String ACCESS_TOKEN = "AccessToken";
    public static final String REFRESH_TOKEN = "RefreshToken";

    @Value("${spring.security.jwt.access.expiration}")
    private long accessTokenExpirationPeriod;

    @Value("${spring.security.jwt.refresh.expiration}")
    private long refreshTokenExpirationPeriod;

    @Value("${spring.security.jwt.access.header}")
    private String accessHeader;

    @Value("${spring.security.jwt.refresh.header}")
    private String refreshHeader;

    @Value("${spring.security.jwt.secret-key}")
    private String secretCode;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secretCode.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long userId, UserRole role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessTokenExpirationPeriod);

        return Jwts.builder()
                .issuer("paperple")
                .subject(ACCESS_TOKEN)
                .claim(USER_ID_CLAIM, userId)
                .claim(ROLE_CLAIM, role.name())
                .expiration(expireDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenExpirationPeriod);

        return Jwts.builder()
                .issuer("paperple")
                .subject(REFRESH_TOKEN)
                .claim(USER_ID_CLAIM, userId)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT token 입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 서명 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public JwtClaimResponse extractClaims(String token) {
        Claims payload = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long userId = payload.get(USER_ID_CLAIM, Long.class);
        UserRole userRole = UserRole.valueOf(payload.get(ROLE_CLAIM, String.class));

        return new JwtClaimResponse(userId, userRole);
    }
}
