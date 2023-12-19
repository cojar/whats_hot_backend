package com.cojar.whats_hot_backend.domain.member_module.member.service;

import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.MemberRole;
import com.cojar.whats_hot_backend.domain.member_module.member.repository.MemberRepository;
import com.cojar.whats_hot_backend.domain.member_module.member.request.MemberRequest;
import com.cojar.whats_hot_backend.domain.member_module.member_image.entity.MemberImage;
import com.cojar.whats_hot_backend.global.jwt.JwtProvider;
import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public boolean hasNoMember() {
        return this.memberRepository.count() == 0;
    }

    @Transactional
    public Member signup(MemberRequest.Signup request, List<MemberRole> authorities) throws IOException {

        Member member = Member.builder()
                .username(request.getUsername())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .authorities(authorities)
                .build();

        this.memberRepository.save(member);

        return member;
    }

    public ResData signupValidate(MemberRequest.Signup request, Errors errors) {

        if (errors.hasErrors()) {
            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-01-01-01",
                    "요청 값이 올바르지 않습니다",
                    errors
            );
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {

            errors.rejectValue("passwordConfirm", "not matched", "passwordConfirm does not matched with password");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-01-01-02",
                    "비밀번호가 서로 일치하지 않습니",
                    errors
            );
        }

        if (this.memberRepository.existsByUsername(request.getUsername())) {

            errors.rejectValue("username", "unique violation", "username unique violation");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-01-01-03",
                    "이미 존재하는 아이디입니다",
                    errors
            );
        }

        return null;
    }

    public Member getUserByUsername(String username) {
        return this.memberRepository.findByUsername(username)
                .orElse(null);
    }

    public ResData loginValidate(MemberRequest.Login loginReq, Errors errors) {

        if (errors.hasErrors()) {
            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-01-02-01",
                    "요청 값이 올바르지 않습니다",
                    errors
            );
        }

        Member member= this.memberRepository.findByUsername(loginReq.getUsername())
                .orElse(null);
        if (member == null) {

            errors.rejectValue("username", "not exist", "member does not exist");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-01-02-02",
                    "존재하지 않는 회원입니다",
                    errors
            );
        }

        if (!this.passwordEncoder.matches(loginReq.getPassword(), member.getPassword())) {

            errors.rejectValue("password", "not matched", "password is not matched");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-01-02-03",
                    "비밀번호가 일치하지 않습니다",
                    errors
            );
        }

        return null;
    }

    @Transactional
    public String getAccessToken(MemberRequest.Login loginReq) {

        Member member = this.memberRepository.findByUsername(loginReq.getUsername())
                .orElse(null);

        member = member.toBuilder()
                .isLogout(false)
                .build();

        this.memberRepository.save(member);

        return this.jwtProvider.genToken(member.toClaims(), 60 * 60 * 24 * 365); // 1년 유효 토큰 생성
    }

    @Transactional
    public void logout(Member member) {

        member = member.toBuilder()
                .isLogout(true)
                .build();

        this.memberRepository.save(member);
    }

    @Transactional
    public Member updateProfileImage(Member member, MemberImage profileImage) {

        member = member.toBuilder()
                .profileImage(profileImage)
                .build();

        this.memberRepository.save(member);

        return member;
    }
}
