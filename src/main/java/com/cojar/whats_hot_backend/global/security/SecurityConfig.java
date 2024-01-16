package com.cojar.whats_hot_backend.global.security;

import com.cojar.whats_hot_backend.global.errors.exception_handler.ApiAuthenticationExceptionHandler;
import com.cojar.whats_hot_backend.global.errors.exception_handler.ApiAuthorizationExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final ApiAuthenticationExceptionHandler apiAuthenticationExceptionHandler;
    private final ApiAuthorizationExceptionHandler apiAuthorizationExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/api/**") // 아래의 모든 설정 /api/** 경로에만 적용
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/api/members").permitAll() // post:/api/members 아무나 접속 가능
                        .requestMatchers(HttpMethod.POST, "/api/members/login").permitAll() // post:/api/members/login 아무나 접속 가능
                        .requestMatchers(HttpMethod.POST, "/api/members/username").permitAll() // post:/api/members/me/username 아무나 접속 가능
                        .requestMatchers(HttpMethod.POST, "/api/members/password").permitAll() // post:/api/members/me/username 아무나 접속 가능
                        .requestMatchers(HttpMethod.POST, "/api/spots").hasAuthority("admin") // post:/api/spots 관리자만 접속 가능
                        .requestMatchers(HttpMethod.GET, "/api/spots").permitAll() // get:/api/spots 아무나 접속 가능
                        .requestMatchers(HttpMethod.GET, "/api/spots/*").permitAll() // get:/api/spots/* 아무나 접속 가능
                        .requestMatchers(HttpMethod.PATCH, "/api/spots/*").hasAuthority("admin") // patch:/api/spots/* 관리자만 접속 가능
                        .requestMatchers(HttpMethod.DELETE, "/api/spots/*").hasAuthority("admin") // delete:/api/spots/* 관리자만 접속 가능
                        .requestMatchers(HttpMethod.GET, "/api/reviews").permitAll() // get:/api/reviews 아무나 접속 가능
                        .requestMatchers(HttpMethod.GET, "/api/reviews/*").permitAll() // get:/api/reviews/* 아무나 접속 가능
                        .requestMatchers(HttpMethod.GET, "/api/comments/*").permitAll() // get:/api/comments/* 아무나 접속 가능
                        .anyRequest().authenticated() // 그 외는 인증된 사용자만 접속 가능
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(apiAuthenticationExceptionHandler) // 인증 에러
                        .accessDeniedHandler(apiAuthorizationExceptionHandler)
                )
                .cors(cors -> corsConfigurationSource()
                )
                .csrf(csrf -> csrf
                        .disable() // CSRF 토큰 끄기
                )
                .httpBasic(httpBasic -> httpBasic
                        .disable() // httpBasic 로그인 방식 끄기
                )
                .formLogin(formLogin -> formLogin
                        .disable() // 폼 로그인 방식 끄기
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 끄기
                )
                .addFilterBefore( // b filter 실행 전 a filter 실행
                        jwtAuthorizationFilter, // 강제 인증 할당 메서드 실행
                        UsernamePasswordAuthenticationFilter.class
                )
        ;

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/api/index", "/api/swagger-ui/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173", "https://whats-hot-frontend.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
