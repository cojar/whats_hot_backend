package com.cojar.whats_hot_backend.domain.member_module.member.controller;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.base_module.mail.service.MailService;
import com.cojar.whats_hot_backend.domain.index_module.index.controller.IndexController;
import com.cojar.whats_hot_backend.domain.member_module.member.api_response.MemberApiResponse;
import com.cojar.whats_hot_backend.domain.member_module.member.dto.MemberDto;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.MemberRole;
import com.cojar.whats_hot_backend.domain.member_module.member.request.MemberRequest;
import com.cojar.whats_hot_backend.domain.member_module.member.response.MemberResponse;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.member_module.member_image.entity.MemberImage;
import com.cojar.whats_hot_backend.domain.member_module.member_image.service.MemberImageService;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Tag(name = "Member", description = "회원 서비스 API")
@RequestMapping(value = "/api/members", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberImageService memberImageService;
    private final FileService fileService;
    private final MailService mailService;

    @MemberApiResponse.Signup
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity signup(@Valid @RequestPart(value = "request") MemberRequest.Signup request, Errors errors,
                                 @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        ResData resData = this.memberService.signupValidate(request, errors);
        if (resData != null) return ResponseEntity.badRequest().body(resData);

        Member member = this.memberService.signup(request, List.of(MemberRole.USER));

        if (profileImage != null) {
            resData = this.fileService.validateUnit(profileImage);
            if (resData != null) return ResponseEntity.badRequest().body(resData);
            _File file = this.fileService.create(profileImage, FileDomain.MEMBER);
            MemberImage _profileImage = this.memberImageService.create(member, file);
            member.updateProfileImage(_profileImage);
        }

        member = this.memberService.save(member);

        resData = ResData.of(
                ResCode.S_01_01,
                MemberDto.of(member),
                linkTo(this.getClass()).slash("login")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Member/signup").withRel("profile"));
        return ResponseEntity.created(linkTo(this.getClass()).slash("me").toUri())
                .body(resData);
    }

    @MemberApiResponse.Login
    @PostMapping(value = "/login")
    public ResponseEntity login(@Valid @RequestBody MemberRequest.Login loginReq, Errors errors) {

        ResData resData = this.memberService.loginValidate(loginReq, errors);
        if (resData != null) return ResponseEntity.badRequest().body(resData);

        String accessToken = this.memberService.getAccessToken(loginReq);

        resData = ResData.of(
                ResCode.S_01_02,
                new MemberResponse.Login(accessToken),
                linkTo(IndexController.class).slash("/api/index")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Member/login").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @MemberApiResponse.Logout
    @PostMapping(value = "/logout", consumes = MediaType.ALL_VALUE)
    public ResponseEntity logout(@AuthenticationPrincipal User user) {

        Member member = this.memberService.getUserByUsername(user.getUsername());
        this.memberService.logout(member);

        ResData resData = ResData.of(
                ResCode.S_01_03,
                linkTo(IndexController.class).slash("/api/index")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Member/logout").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @MemberApiResponse.Me
    @GetMapping(value = "/me", consumes = MediaType.ALL_VALUE)
    public ResponseEntity me(@AuthenticationPrincipal User user) {

        Member member = this.memberService.getUserByUsername(user.getUsername());

        ResData resData = ResData.of(
                ResCode.S_01_04,
                new MemberResponse.Me(MemberDto.of(member)),
                linkTo(this.getClass()).slash("me")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Member/me").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @MemberApiResponse.UpdatePassword
    @PatchMapping(value = "/password")
    public ResponseEntity updatePassword(@Valid @RequestBody MemberRequest.UpdatePassword request, Errors errors,
                                         @AuthenticationPrincipal User user) {

        Member member = this.memberService.getUserByUsername(user.getUsername());

        ResData resData = this.memberService.updatePasswordValidate(request, member, errors);
        if (resData != null) return ResponseEntity.badRequest().body(resData);

        this.memberService.updatePassword(request, member);

        resData = ResData.of(
                ResCode.S_01_05,
                linkTo(this.getClass()).slash("me")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Member/updatePassword").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @MemberApiResponse.FindUsername
    @PostMapping(value = "/username")
    public ResponseEntity findUsername(@Valid @RequestBody MemberRequest.FindUsername request, Errors errors) {

        ResData resData = this.memberService.findUsernameValidate(request, errors);
        if (resData != null) return ResponseEntity.badRequest().body(resData);

        Member member = this.memberService.getUserByEmail(request.getEmail());

        resData = ResData.of(
                ResCode.S_01_06,
                new MemberResponse.FindUsername(member.getUsername()),
                linkTo(this.getClass()).slash("login")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Member/findUsername").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @MemberApiResponse.FindPassword
    @PostMapping(value = "/password")
    public ResponseEntity findPassword(@Valid @RequestBody MemberRequest.FindPassword request, Errors errors) throws MessagingException {

        ResData resData = this.memberService.findPasswordValidate(request, errors);
        if (resData != null) return ResponseEntity.badRequest().body(resData);

        Member member = this.memberService.getUserByUsernameAndEmail(request);

        String resetPassword = AppConfig.getRandomPassword();
        this.mailService.send(member.getEmail(), resetPassword, "임시 비밀번호"); // exception 발생 시 저장 안 되도록
        this.memberService.updatePassword(member, resetPassword);

        resData = ResData.of(
                ResCode.S_01_07,
                linkTo(this.getClass()).slash("login")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Member/findPassword").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }
}
