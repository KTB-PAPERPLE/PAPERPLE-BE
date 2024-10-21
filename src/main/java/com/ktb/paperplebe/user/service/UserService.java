package com.ktb.paperplebe.user.service;

import com.ktb.paperplebe.user.dto.UserInfoResponse;
import com.ktb.paperplebe.user.entity.User;
import com.ktb.paperplebe.user.exception.UserException;
import com.ktb.paperplebe.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ktb.paperplebe.user.exception.UserErrorCode.USER_NOT_FOUND;
import static com.ktb.paperplebe.user.exception.UserErrorCode.NICKNAME_DUPLICATED;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    public UserInfoResponse getUserInfo(Long id) {
        User user = findById(id);
        return UserInfoResponse.of(user);
    }

    @Transactional
    public UserInfoResponse updateProfileImage(Long userId, String profileImage) {
        User user = findById(userId);
        user.updateProfileImage(profileImage);
        return UserInfoResponse.of(user);
    }

    @Transactional
    public UserInfoResponse updateNickname(Long userId, String nickname) {
        User user = findById(userId);

        validateDuplicatedNickname(nickname);

        user.updateNickname(nickname);
        return UserInfoResponse.of(user);
    }

    private void validateDuplicatedNickname(String nickname) {
        if (existsByNickname(nickname)) {
            System.out.println("@@@ here");
            throw new UserException(NICKNAME_DUPLICATED);
        }
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
