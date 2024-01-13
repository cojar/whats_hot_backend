package com.cojar.whats_hot_backend.global.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiAuthenticationEntrypoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

//         rs = RsData.of("F-AccessDenied", "인증에 실패했습니다");
//
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType(APPLICATION_JSON_VALUE);
//        response.setStatus(403);
//        response.getWriter().append(new Gson().toJson(rs));
    }
}
