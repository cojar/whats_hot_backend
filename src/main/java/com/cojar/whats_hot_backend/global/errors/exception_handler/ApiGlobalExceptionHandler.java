package com.cojar.whats_hot_backend.global.errors.exception_handler;

import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.ResData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = {RestController.class})
public class ApiGlobalExceptionHandler {

    @ExceptionHandler({ApiResponseException.class})
    public ResponseEntity apiResponseExceptionHandler(ApiResponseException exception) {

        ResData resData = exception.getResData();
        return ResponseEntity.status(resData.getStatus()).body(resData);
    }
}
