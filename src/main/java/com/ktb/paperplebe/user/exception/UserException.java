package com.ktb.paperplebe.user.exception;

import com.ktb.paperplebe.common.exception.DomainException;

public class UserException extends DomainException {
    public UserException(final UserErrorCode errorCode)  {
        super(errorCode);
    }
}
