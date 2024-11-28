package com.ktb.paperplebe.auth.config.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Entity
@Getter
@NoArgsConstructor
@RedisHash("refreshToken")
public class RefreshToken {
  @Id private String refreshToken;
  private Long userId;

  public RefreshToken(String refreshToken, Long userId) {
    this.refreshToken = refreshToken;
    this.userId = userId;
  }
}
