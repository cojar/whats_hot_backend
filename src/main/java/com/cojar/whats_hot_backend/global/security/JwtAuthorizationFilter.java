package com.cojar.whats_hot_backend.global.security;

import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.repository.MemberRepository;
import com.cojar.whats_hot_backend.global.errors.exception.DataNotFoundException;
import com.cojar.whats_hot_backend.global.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 보통 토큰 저장 헤더 키 == "Authorization", 값 == "Bearer **"
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) { // 토큰 존재 여부 체크

            String accessToken = bearerToken.substring("Bearer ".length()); // 토큰 추출, O(1)

            if (this.jwtProvider.verify(accessToken)) { // 토큰 검증

                long id = (int) this.jwtProvider.getClaims(accessToken).get("id"); // username 추출

                Member member = this.memberRepository.findById(id)
                        .orElseThrow(() -> new DataNotFoundException("user not found on id " + id)); // member 가져옴

                if (!member.isLogout) this.forceAuthentication(member); // 강제 인증 할당
            }

        }

        filterChain.doFilter(request, response);
    }

    // 강제 인증 할당 메서드
    private void forceAuthentication(Member member) {

        // member로부터 user 객체 생성
        User user = new User(member.getUsername(), member.getPassword(), member.getAuthorities());

        // 스프링 시큐리티 내에 저장할 authenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                UsernamePasswordAuthenticationToken.authenticated(
                        user,
                        null,
                        member.getAuthorities()
                );

        // 스프링 시큐리티 내에 생성한 authenticationToken 객체를 저장할 context 객체 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // context 객체에 authenticationToken 객체 저장
        context.setAuthentication(authenticationToken);
        // 스프링 시큐리티에 context 등록
        SecurityContextHolder.setContext(context);
    }
}
