package com.ktb.paperplebe.auth.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb.paperplebe.auth.dto.JwtClaimResponse;
import com.ktb.paperplebe.auth.service.TokenService;
import com.ktb.paperplebe.common.dto.ExceptionResponse;
import com.ktb.paperplebe.common.service.CookieService;
import com.ktb.paperplebe.user.constant.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.ktb.paperplebe.auth.config.jwt.JwtUtil.ACCESS_TOKEN;
import static com.ktb.paperplebe.auth.config.jwt.JwtUtil.REFRESH_TOKEN;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;
    private final CookieService cookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        // h2-console 요청은 토큰 검사를 수행하지 않음
        log.info("doFilterInternal - 토큰 검사 수행");
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/h2-console") || requestURI.startsWith("/health") ) {
            log.info("토큰 검사를 수행하지 않고 넘어감.");
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.info("request의 쿠키가 비어있음.");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = extractCookie(cookies, ACCESS_TOKEN);
        String refreshToken = extractCookie(cookies, REFRESH_TOKEN);
        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);

        // case1. accessToken이 유효한 경우
        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
            log.info("case1");
            setAuthInSecurityContext(accessToken);
            filterChain.doFilter(request, response);
            return;
        }

        // case2. accessToken이 유효하지 않고 refreshToken이 유효하면 accessToken 갱신
        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            log.info("case2");
            accessToken = tokenService.renewToken(response, refreshToken);
            setAuthInSecurityContext(accessToken);
            filterChain.doFilter(request, response);
            return;
        }

        // case3. accessToken과 refreshToken이 모두 유효하지 않으면 에러 응답 반환
        if (accessToken != null) {
            log.info("case3");
            cookieService.deleteCookie(response, ACCESS_TOKEN);
        }

        if (!jwtUtil.validateToken(refreshToken)) {
            log.info("refresh token이 유효하지 않음.");
            cookieService.deleteCookie(response, REFRESH_TOKEN);
            tokenService.deleteRefreshToken(refreshToken);
        }

        issueErrorResponse(response);
    }

    private String extractCookie(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null); // 쿠키가 존재하지 않을 경우 null 반환
    }

    private void issueErrorResponse(HttpServletResponse response) throws IOException {
        ExceptionResponse exceptionResponse = new ExceptionResponse("토큰이 유효하지 않습니다.", 403);
        response.setStatus(403);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
    }

    private void setAuthInSecurityContext(String accessToken) {
        JwtClaimResponse claimDTO = jwtUtil.extractClaims(accessToken);

        Long userId = claimDTO.getUserId();
        UserRole role = claimDTO.getRole();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userId, null, List.of(role::name));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
