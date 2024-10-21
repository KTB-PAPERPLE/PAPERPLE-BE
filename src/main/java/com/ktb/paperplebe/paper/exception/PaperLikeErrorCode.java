package com.ktb.paperplebe.paper.exception;

import com.ktb.paperplebe.common.exception.DomainErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PaperLikeErrorCode implements DomainErrorCode {
    PAPER_NOT_FOUND("요청하신 페이퍼를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PAPER_ALREADY_LIKED("이미 좋아요를 눌렀습니다.", HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus status;

    PaperLikeErrorCode(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
