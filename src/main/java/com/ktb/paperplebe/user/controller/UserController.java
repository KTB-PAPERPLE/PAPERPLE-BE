package com.ktb.paperplebe.user.controller;

import com.ktb.paperplebe.user.constant.UserRole;
import com.ktb.paperplebe.user.dto.UserInfoResponse;
import com.ktb.paperplebe.user.dto.UserNicknameRequest;
import com.ktb.paperplebe.user.dto.UserProfileImageRequest;
import com.ktb.paperplebe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserService userService;

    @Secured(UserRole.ROLE_USER_VALUE)
    @GetMapping()
    public UserInfoResponse getUserInfo(
            @AuthenticationPrincipal Long userId
    ) {
        return userService.getUserInfo(userId);
    }

    @Secured(UserRole.ROLE_USER_VALUE)
    @PatchMapping("/nickname")
    public UserInfoResponse updateNickname(
            @RequestBody @Validated UserNicknameRequest nicknameRequest,
            @AuthenticationPrincipal Long userId
    ) {
        return userService.updateNickname(userId, nicknameRequest.getNickname());
    }

    @Secured(UserRole.ROLE_USER_VALUE)
    @PatchMapping("/profile-image")
    public UserInfoResponse updateProfileImage(
            @RequestBody UserProfileImageRequest profileImageRequest,
            @AuthenticationPrincipal Long userId
    ) {
        return userService.updateProfileImage(userId, profileImageRequest.getProfileImage());
    }
}
