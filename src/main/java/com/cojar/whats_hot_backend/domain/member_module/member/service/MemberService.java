package com.cojar.whats_hot_backend.domain.member_module.member.service;

import com.cojar.whats_hot_backend.domain.member_module.member.request.MemberRequest;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.MemberRole;
import com.cojar.whats_hot_backend.domain.member_module.member.repository.MemberRepository;
import com.cojar.whats_hot_backend.global.jwt.JwtProvider;
import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Member getUserByUsername(String username) {
        return this.memberRepository.findByUsername(username)
                .orElse(null);
    }

    public ResData loginValidate(MemberRequest.Login loginReq, Errors errors) {

        if (errors.hasErrors()) {
            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-01-01",
                    "요청 값이 올바르지 않습니다",
                    errors
            );
        }

        Member member= this.memberRepository.findByUsername(loginReq.getUsername())
                .orElse(null);
        if (member == null) {
            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-01-02",
                    "존재하지 않는 회원입니다"
            );
        }

        if (!this.passwordEncoder.matches(loginReq.getPassword(), member.getPassword())) {

            errors.rejectValue("password", "not matched", "password is not matched");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-01-03",
                    "비밀번호가 일치하지 않습니다",
                    errors
            );
        }

        return null;
    }

    public boolean hasNoMember() {
        return this.memberRepository.count() == 0;
    }

    public Member signup(String username, String password, String email, List<MemberRole> authorities) {

        Member member = Member.builder()
                .username(username)
                .password(this.passwordEncoder.encode(password))
                .email(email)
                .authorities(authorities)
                .build();

        this.memberRepository.save(member);

        return member;
    }

    public String getAccessToken(MemberRequest.Login loginReq) {

        Member member = this.memberRepository.findByUsername(loginReq.getUsername())
                .orElse(null);

        member = member.toBuilder()
                .isLogout(false)
                .build();

        this.memberRepository.save(member);

        return this.jwtProvider.genToken(member.toClaims(), 60 * 60 * 24 * 365); // 1년 유효 토큰 생성
    }

    public void logout(Member member) {

        member = member.toBuilder()
                .isLogout(true)
                .build();

        this.memberRepository.save(member);
    }
}
