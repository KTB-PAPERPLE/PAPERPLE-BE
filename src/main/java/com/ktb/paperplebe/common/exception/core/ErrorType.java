package com.ktb.paperplebe.common.exception.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    AUTH("인증/인가 관련 에러"),

    USER("회원 관련 에러");

    private final String description;
}
