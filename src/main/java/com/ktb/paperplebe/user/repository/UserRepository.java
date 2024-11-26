package com.ktb.paperplebe.user.repository;

import com.ktb.paperplebe.oauth.constant.SocialType;
import com.ktb.paperplebe.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findById(Long id);

  Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

  boolean existsByNickname(String nickname);
}
