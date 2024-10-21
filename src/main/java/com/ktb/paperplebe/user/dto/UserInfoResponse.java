package com.ktb.paperplebe.user.dto;

import com.ktb.paperplebe.user.entity.User;

public record UserInfoResponse(
        Long userId,
        String nickname,
        String profileImage
) {
    public static UserInfoResponse of(final User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getNickname(),
                user.getProfileImage()
        );
    }
}
