package com.ktb.paperplebe.common.dto;

import com.ktb.paperplebe.common.exception.core.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponse {
    private boolean isSuccessful = false;
    private Integer statusCode;
    private String errorCode;
    private String message;

    public ExceptionResponse(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    private ExceptionResponse(ErrorCode errorCode) {
        this.statusCode = errorCode.getStatusCode();
        this.errorCode = errorCode.name();
        this.message = errorCode.getMessage();
    }
    public static ExceptionResponse of(ErrorCode errorCode) {
        return new ExceptionResponse(errorCode);
    }
}
