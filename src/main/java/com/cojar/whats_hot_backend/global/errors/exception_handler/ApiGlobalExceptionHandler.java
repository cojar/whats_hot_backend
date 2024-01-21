package com.cojar.whats_hot_backend.global.errors.exception_handler;

import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ApiGlobalExceptionHandler {

    @ExceptionHandler({ApiResponseException.class})
    public ResponseEntity apiResponseExceptionHandler(ApiResponseException exception) {

        ResData resData = exception.getResData();
        return ResponseEntity.status(resData.getStatus()).body(resData);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception) {

        ResData resData = ResData.of(
                ResCode.F_99_99_04
        );
        resData.add(Link.of(AppConfig.getIndexURL()).withRel("index"));
        return ResponseEntity.badRequest().body(resData);
    }

    @ExceptionHandler({MissingServletRequestPartException.class})
    public ResponseEntity missingServletRequestPartExceptionHandler(MissingServletRequestPartException exception) {

        Errors errors = AppConfig.getMockErrors();

        errors.reject("not exist", new Object[]{exception.getRequestPartName()}, "request part does not exist");

        ResData resData = ResData.of(
                ResCode.F_99_99_05,
                errors
        );

        return ResponseEntity.badRequest().body(resData);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception) {

        Errors errors = AppConfig.getMockErrors();

        errors.reject("wrong type", new Object[]{exception.getName()}, "input parameter has wrong type");

        ResData resData = ResData.of(
                ResCode.F_99_99_06,
                errors
        );

        return ResponseEntity.badRequest().body(resData);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity runtimeExceptionHandler(RuntimeException exception) {

        log.info(exception.toString());
        log.info(exception.getCause().toString());

        ResData resData = ResData.of(
                ResCode.F_99_99_99
        );

        return ResponseEntity.badRequest().body(resData);
    }
}
