package com.ktb.paperplebe.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SuccessResponse {
    private boolean isSuccessful = true;
    private Integer statusCode = 200;

    public SuccessResponse(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public SuccessResponse() {
    }
}
