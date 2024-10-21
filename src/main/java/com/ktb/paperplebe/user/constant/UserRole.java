package com.ktb.paperplebe.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ROLE_USER, ROLE_ADMIN;

    public static final String ROLE_USER_VALUE = "ROLE_USER";
    public static final String ROLE_ADMIN_VALUE = "ROLE_ADMIN";
}
