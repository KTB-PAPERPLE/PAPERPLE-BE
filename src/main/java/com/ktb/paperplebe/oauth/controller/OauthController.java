package com.ktb.paperplebe.oauth.controller;

import com.ktb.paperplebe.auth.config.jwt.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ktb.paperplebe.auth.config.jwt.JwtUtil.ACCESS_TOKEN;
import static com.ktb.paperplebe.auth.config.jwt.JwtUtil.REFRESH_TOKEN;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {

    private final RefreshTokenRepository refreshTokenRepository;

    @GetMapping("/login-info")
    public ResponseEntity<Map<String, Object>> oauthLoginInfo(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        return ResponseEntity.status(HttpStatus.OK)
                .body(attributes);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키 삭제
        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(0); // 쿠키 즉시 만료
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN, null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(0); // 쿠키 즉시 만료
        response.addCookie(refreshTokenCookie);

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REFRESH_TOKEN.equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken != null) {
            refreshTokenRepository.deleteById(refreshToken);
        }

        return ResponseEntity.ok("Logged out successfully");
    }
}