package com.ktb.paperplebe.user.exception;

import com.ktb.paperplebe.common.exception.DomainErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements DomainErrorCode {
    NICKNAME_DUPLICATED("이미 존재하는 닉네임입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("요청하신 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    UserErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
