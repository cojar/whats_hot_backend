package com.cojar.whats_hot_backend.global.errors.exception;

import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.Getter;

@Getter
public class ApiResponseException extends RuntimeException {

    private final ResData resData;

    public ApiResponseException(ResData resData) {
        super("response failed");
        this.resData = resData;
    }
}
