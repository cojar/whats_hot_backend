package com.cojar.whats_hot_backend.domain.member_module.member.controller;

import com.cojar.whats_hot_backend.domain.base_module.mail.service.MailService;
import com.cojar.whats_hot_backend.domain.index_module.index.controller.IndexController;
import com.cojar.whats_hot_backend.domain.member_module.member.api_response.MemberApiResponse;
import com.cojar.whats_hot_backend.domain.member_module.member.dto.MemberDto;
import com.cojar.whats_hot_backend.domain.member_module.member.dto.MemberTokenDto;
import com.cojar.whats_hot_backend.domain.member_module.member.dto.MemberUsernameDto;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.request.MemberRequest;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Tag(name = "Member", description = "회원 서비스 API")
@RequestMapping(value = "/api/members", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MailService mailService;

    @MemberApiResponse.Signup
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity signup(@Valid @RequestPart(value = "request") MemberRequest.Signup request, Errors errors,
                                 @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        Member member = this.memberService.signup(request, profileImage, false, errors);

        ResData resData = ResData.of(
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
    public ResponseEntity login(@Valid @RequestBody MemberRequest.Login request, Errors errors) {

        String accessToken = this.memberService.login(request, errors);

        ResData resData = ResData.of(
                ResCode.S_01_02,
                MemberTokenDto.of(accessToken),
                linkTo(IndexController.class).slash("/api/index")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Member/login").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @MemberApiResponse.Logout
    @PostMapping(value = "/logout", consumes = MediaType.ALL_VALUE)
    public ResponseEntity logout(@AuthenticationPrincipal User user) {

        this.memberService.logout(user);

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
                MemberDto.of(member),
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

        this.memberService.updatePassword(request, user, errors);

        ResData resData = ResData.of(
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

        Member member = this.memberService.findUsername(request, errors);

        ResData resData = ResData.of(
                ResCode.S_01_06,
                MemberUsernameDto.of(member),
                linkTo(this.getClass()).slash("login")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Member/findUsername").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @MemberApiResponse.FindPassword
    @PostMapping(value = "/password")
    public ResponseEntity findPassword(@Valid @RequestBody MemberRequest.FindPassword request, Errors errors) throws MessagingException {

        this.memberService.findPassword(request, errors);

        ResData resData = ResData.of(
                ResCode.S_01_07,
                linkTo(this.getClass()).slash("login")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Member/findPassword").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }
}
