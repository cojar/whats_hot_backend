package com.cojar.whats_hot_backend.global.errors.exception_handler;

import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiAuthenticationExceptionHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType(MediaTypes.HAL_JSON_VALUE);
        response.setStatus(ResCode.F_99_99_01.getStatus().value());
        ResData resData = ResData.of(ResCode.F_99_99_01);
        resData.add(Link.of(AppConfig.getIndexURL()).withRel("index"));
        response.getWriter().write(AppConfig.responseSerialize(resData));
    }
}
