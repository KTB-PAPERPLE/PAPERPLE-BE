package com.ktb.paperplebe.auth.service;

import static com.ktb.paperplebe.auth.config.jwt.JwtUtil.ACCESS_TOKEN;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.ktb.paperplebe.auth.config.jwt.JwtUtil;
import com.ktb.paperplebe.auth.config.refreshtoken.RefreshToken;
import com.ktb.paperplebe.auth.config.refreshtoken.RefreshTokenRepository;
import com.ktb.paperplebe.common.service.CookieService;
import com.ktb.paperplebe.user.entity.User;
import com.ktb.paperplebe.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final CookieService cookieService;

  public String renewToken(HttpServletResponse response, String refreshToken) {
    String renewAccessToken = createAccessToken(refreshToken);
    ResponseCookie accessCookie = cookieService.createCookie(ACCESS_TOKEN, renewAccessToken);
    response.addHeader(SET_COOKIE, accessCookie.toString());
    return accessCookie.getValue();
  }

  public String createAccessToken(final String refreshToken) {
    RefreshToken findRefreshToken =
        refreshTokenRepository
            .findById(refreshToken)
            .orElseThrow(() -> new EntityNotFoundException("Refresh Token not found"));

    User user =
        userRepository
            .findById(findRefreshToken.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    return jwtUtil.generateAccessToken(findRefreshToken.getUserId(), user.getRole());
  }

  public void saveRefreshToken(String refreshToken, Long userId) {
    deleteRefreshToken(refreshToken);
    refreshTokenRepository.save(new RefreshToken(refreshToken, userId));
  }

  public void deleteRefreshToken(String refreshToken) {
    refreshTokenRepository.deleteById(refreshToken);
  }
}
