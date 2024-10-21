package com.ktb.paperplebe.common.exception;

import org.springframework.http.HttpStatus;

public interface DomainErrorCode {
    HttpStatus getStatus();
    String getMessage();
}
