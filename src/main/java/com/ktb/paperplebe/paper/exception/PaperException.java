package com.ktb.paperplebe.paper.exception;

import com.ktb.paperplebe.common.exception.DomainException;

public class PaperException extends DomainException {
    public PaperException(final PaperErrorCode errorCode)  {
        super(errorCode);
    }
}
