package com.ktb.paperplebe.common.exception.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_LOGIN_INFO(400, "로그인 정보가 올바르지 않습니다.", ErrorType.AUTH, ""),

    ALL_AUTH_TOKEN_EXPIRED(401, "엑세스 토큰과 리프레시 토큰이 모두 만료되었습니다. 다시 로그인해 주세요.", ErrorType.AUTH, ""),

    UNAUTHORIZED_ACCESS(403, "해당 리소스에 접근 권한이 없습니다.", ErrorType.AUTH, "");

    private final Integer statusCode;
    private final String message;
    private final ErrorType errorType;
    private final String description;

    public String getDescription() {
        if (description.isEmpty()) return message;
        return description;
    }

    public static List<ErrorCode> getErrorCodes(ErrorType errorType) {
        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getErrorType() == errorType)
                .collect(Collectors.toList());
    }
}
