package com.ktb.paperplebe.auth.config.refreshtoken;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash("refreshToken")
public class RefreshToken {

  @Id
  private String refreshToken;
  private Long userId;

  public RefreshToken(String refreshToken, Long userId) {
    this.refreshToken = refreshToken;
    this.userId = userId;
  }
}