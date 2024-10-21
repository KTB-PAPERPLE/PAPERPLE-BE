package com.ktb.paperplebe.oauth.constant;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum SocialType {
    KAKAO("kakao"), NONE("");

    private final String id;

    SocialType(String id) {
        this.id = id;
    }

    public static SocialType getSocialType(String registrationId) {
        return Arrays.stream(values())
                .filter(type -> type.id.equals(registrationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid social type: " + registrationId));
    }
}