package com.cojar.whats_hot_backend.domain.member_module.member.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.MemberRole;
import com.cojar.whats_hot_backend.domain.member_module.member.repository.MemberRepository;
import com.cojar.whats_hot_backend.domain.member_module.member.request.MemberRequest;
import com.cojar.whats_hot_backend.domain.member_module.member_image.service.MemberImageService;
import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.jwt.JwtProvider;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    private final FileService fileService;
    private final MemberImageService memberImageService;

    private final EntityManager entityManager;

    public boolean hasNoMember() {
        return this.memberRepository.count() == 0;
    }

    public long count() {
        return this.memberRepository.count();
    }

    @Transactional
    public Member save(Member member) {

        return this.memberRepository.save(member);
    }

    @Transactional
    public Member signup(MemberRequest.Signup request, MultipartFile profileImage, boolean isAdmin, Errors errors) {

        // request 에러 검증
        this.signupValidate(request, errors);

        // profileImage 에러 검증
        this.fileService.validateUnit(profileImage);

        Member member = Member.builder()
                .username(request.getUsername())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .authorities(isAdmin ? List.of(MemberRole.ADMIN, MemberRole.USER) : List.of(MemberRole.USER))
                .build();

        this.memberRepository.save(member);

        // images 생성
        _File file = this.fileService.createUnit(profileImage, FileDomain.MEMBER);
        this.memberImageService.create(file, member);

        entityManager.refresh(member);

        return member;
    }

    private void signupValidate(MemberRequest.Signup request, Errors errors) {

        if (errors.hasErrors()) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_01_01,
                            errors
                    )
            );
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {

            errors.rejectValue("passwordConfirm", "not matched", "passwordConfirm does not matched with password");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_01_02,
                            errors
                    )
            );
        }

        if (this.memberRepository.existsByUsername(request.getUsername())) {

            errors.rejectValue("username", "unique violation", "username unique violation");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_01_03,
                            errors
                    )
            );
        }

        if (this.memberRepository.existsByEmail(request.getEmail())) {

            errors.rejectValue("email", "unique violation", "email unique violation");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_01_04,
                            errors
                    )
            );
        }
    }

    public Member getUserById(Long id) {
        return this.memberRepository.findById(id)
                .orElse(null);
    }

    public Member getUserByUsername(String username) {
        return this.memberRepository.findByUsername(username)
                .orElse(null);
    }

    public ResData loginValidate(MemberRequest.Login loginReq, Errors errors) {

        if (errors.hasErrors()) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_02_01,
                            errors
                    )
            );
        }

        Member member = this.memberRepository.findByUsername(loginReq.getUsername())
                .orElse(null);
        if (member == null) {

            errors.rejectValue("username", "not exist", "member does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_02_02,
                            errors
                    )
            );
        }

        if (!this.passwordEncoder.matches(loginReq.getPassword(), member.getPassword())) {

            errors.rejectValue("password", "not matched", "password is not matched");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_02_03,
                            errors
                    )
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

    public ResData updatePasswordValidate(MemberRequest.UpdatePassword request, Member member, Errors errors) {

        if (errors.hasErrors()) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_05_01,
                            errors
                    )
            );
        }

        if (!this.passwordEncoder.matches(request.getOldPassword(), member.getPassword())) {

            errors.rejectValue("oldPassword", "not matched", "oldPassword is not matched");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_05_02,
                            errors
                    )
            );
        }

        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {

            errors.rejectValue("newPasswordConfirm", "not matched", "newPassword is not matched");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_05_03,
                            errors
                    )
            );
        }

        return null;
    }

    @Transactional
    public void updatePassword(MemberRequest.UpdatePassword request, Member member) {

        member = member.toBuilder()
                .password(this.passwordEncoder.encode(request.getNewPassword()))
                .build();

        this.memberRepository.save(member);
    }

    public ResData findUsernameValidate(MemberRequest.FindUsername request, Errors errors) {

        if (errors.hasErrors()) {
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_06_01,
                            errors
                    )
            );
        }

        if (!this.memberRepository.existsByEmail(request.getEmail())) {

            errors.rejectValue("email", "not exist", "member that has email does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_06_02,
                            errors
                    )
            );
        }

        return null;
    }

    public Member getUserByEmail(String email) {

        return this.memberRepository.findByEmail(email)
                .orElse(null);
    }

    public ResData findPasswordValidate(MemberRequest.FindPassword request, Errors errors) {

        if (errors.hasErrors()) {
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_07_01,
                            errors
                    )
            );
        }

        if (!this.memberRepository.existsByUsernameAndEmail(request.getUsername(), request.getEmail())) {

            if (!this.memberRepository.existsByUsername(request.getUsername())) {
                errors.rejectValue("username", "not exist", "member that has username does not exist");
            }

            if (!this.memberRepository.existsByEmail(request.getEmail())) {
                errors.rejectValue("email", "not exist", "member that has email does not exist");
            }

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_01_07_02,
                            errors
                    )
            );
        }

        return null;
    }

    public Member getUserByUsernameAndEmail(MemberRequest.FindPassword request) {
        return this.memberRepository.findByUsernameAndEmail(request.getUsername(), request.getEmail())
                .orElse(null);
    }

    @Transactional
    public void updatePassword(Member member, String password) {

        member = member.toBuilder()
                .password(password)
                .build();

        this.memberRepository.save(member);
    }
}
