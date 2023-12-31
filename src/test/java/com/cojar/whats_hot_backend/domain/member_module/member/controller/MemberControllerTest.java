package com.cojar.whats_hot_backend.domain.member_module.member.controller;

import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.request.MemberRequest;
import com.cojar.whats_hot_backend.domain.member_module.member_image.service.MemberImageService;
import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends BaseControllerTest {

    @Autowired
    private MemberImageService memberImageService;

    @Autowired
    private FileService fileService;

    @Transactional
    @Test
    @DisplayName("post:/api/members - created, S-01-01")
    public void signup_Created() throws Exception {

        // given
        String username = "tester";
        String password = "1234";
        String passwordConfirm = "1234";
        String email = "tester@test.io";
        MemberRequest.Signup request = MemberRequest.Signup.builder()
                .username(username)
                .password(password)
                .passwordConfirm(passwordConfirm)
                .email(email)
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String name = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(name, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "profileImage",
                "%s.%s".formatted(name, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/members")
                        .file(_request)
                        .file(_file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-01-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.username").value(username))
                .andExpect(jsonPath("data.email").value(email))
                .andExpect(jsonPath("data.profileImageUri").exists())
                .andExpect(jsonPath("data.authorities[0]").value("user"))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @ParameterizedTest
    @MethodSource("argsFor_signup_BadRequest_NotBlank")
    @DisplayName("post:/api/members - bad request not blank, F-01-01-01")
    public void signup_BadRequest_NotBlank(String username,
                                           String password,
                                           String passwordConfirm,
                                           String email) throws Exception {

        // given
        List<Long> checkList = getCheckListNotCreated();

        MemberRequest.Signup request = MemberRequest.Signup.builder()
                .username(username)
                .password(password)
                .passwordConfirm(passwordConfirm)
                .email(email)
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String name = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(name, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "profileImage",
                "%s.%s".formatted(name, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/members")
                        .file(_request)
                        .file(_file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-01-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(""))
        ;

        checkNotCreated(checkList);
    }

    private static Stream<Arguments> argsFor_signup_BadRequest_NotBlank() {
        return Stream.of(
                Arguments.of("", "1234", "1234", "tester@test.io"),
                Arguments.of("tester", "", "1234", "tester@test.io"),
                Arguments.of("tester", "1234", "", "tester@test.io"),
                Arguments.of("tester", "1234", "1234", "")
        );
    }

    @Test
    @DisplayName("post:/api/members - bad request password not matched, F-01-01-02")
    public void signup_BadRequest_PasswordNotMatched() throws Exception {

        // given
        List<Long> checkList = getCheckListNotCreated();

        String username = "tester";
        String password = "1234";
        String passwordConfirm = "1235";
        String email = "tester@test.io";
        MemberRequest.Signup request = MemberRequest.Signup.builder()
                .username(username)
                .password(password)
                .passwordConfirm(passwordConfirm)
                .email(email)
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String name = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(name, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "profileImage",
                "%s.%s".formatted(name, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/members")
                        .file(_request)
                        .file(_file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-01-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(passwordConfirm))
        ;

        checkNotCreated(checkList);
    }

    @Test
    @DisplayName("post:/api/members - bad request username unique violation, F-01-01-03")
    public void signup_BadRequest_UsernameUniqueViolation() throws Exception {

        // given
        List<Long> checkList = getCheckListNotCreated();

        String username = "user1";
        String password = "1234";
        String passwordConfirm = "1234";
        String email = "tester@test.io";
        MemberRequest.Signup request = MemberRequest.Signup.builder()
                .username(username)
                .password(password)
                .passwordConfirm(passwordConfirm)
                .email(email)
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String name = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(name, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "profileImage",
                "%s.%s".formatted(name, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/members")
                        .file(_request)
                        .file(_file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-01-03"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(username))
        ;

        checkNotCreated(checkList);
    }

    @Test
    @DisplayName("post:/api/members - bad request email unique violation, F-01-01-04")
    public void signup_BadRequest_EmailUniqueViolation() throws Exception {

        // given
        List<Long> checkList = getCheckListNotCreated();

        String username = "tester";
        String password = "1234";
        String passwordConfirm = "1234";
        String email = "user1@test.com";
        MemberRequest.Signup request = MemberRequest.Signup.builder()
                .username(username)
                .password(password)
                .passwordConfirm(passwordConfirm)
                .email(email)
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String name = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(name, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "profileImage",
                "%s.%s".formatted(name, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/members")
                        .file(_request)
                        .file(_file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-01-04"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(email))
        ;

        checkNotCreated(checkList);
    }

    @Test
    @DisplayName("post:/api/members - bad request content type, F-00-00-01")
    public void signup_BadRequest_ContentType() throws Exception {

        // given
        List<Long> checkList = getCheckListNotCreated();

        String username = "tester";
        String password = "1234";
        String passwordConfirm = "1234";
        String email = "tester@test.io";
        MemberRequest.Signup request = MemberRequest.Signup.builder()
                .username(username)
                .password(password)
                .passwordConfirm(passwordConfirm)
                .email(email)
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String name = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(name, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "profileImage",
                "%s.%s".formatted(name, ext),
                MediaType.TEXT_MARKDOWN_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/members")
                        .file(_request)
                        .file(_file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-00-00-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(MediaType.TEXT_MARKDOWN_VALUE))
                .andExpect(jsonPath("_links.index").exists())
        ;

        checkNotCreated(checkList);
    }

    @Test
    @DisplayName("post:/api/members - bad request extension, F-00-00-02")
    public void signup_BadRequest_Extension() throws Exception {

        // given
        List<Long> checkList = getCheckListNotCreated();

        String username = "tester";
        String password = "1234";
        String passwordConfirm = "1234";
        String email = "tester@test.io";
        MemberRequest.Signup request = MemberRequest.Signup.builder()
                .username(username)
                .password(password)
                .passwordConfirm(passwordConfirm)
                .email(email)
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String name = "test";
        String ext = "gif";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(name, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "profileImage",
                "%s.%s".formatted(name, ext),
                MediaType.IMAGE_GIF_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/members")
                        .file(_request)
                        .file(_file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-00-00-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(MediaType.IMAGE_GIF_VALUE))
                .andExpect(jsonPath("_links.index").exists())
        ;

        checkNotCreated(checkList);
    }

    private List<Long> getCheckListNotCreated() {
        return List.of(
                this.memberService.count() + 1,
                this.memberImageService.count(),
                this.fileService.count()
        );
    }

    private void checkNotCreated(List<Long> checkList) {
        int i = 0;
        assertThat(this.memberService.getUserById(checkList.get(i++))).isNull();
        assertThat(this.memberImageService.count()).isEqualTo(checkList.get(i++));
        assertThat(this.fileService.count()).isEqualTo(checkList.get(i));
    }

    @Test
    @DisplayName("post:/api/members/login - ok, S-01-02")
    public void login_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        MemberRequest.Login request = MemberRequest.Login.of(username, password);

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-01-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.accessToken").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @ParameterizedTest
    @MethodSource("argsFor_login_BadRequest_InputValidation")
    @DisplayName("post:/api/members/login - bad request input validation, F-01-02-01")
    public void login_BadRequest_InputValidation(String username, String password) throws Exception {

        // given
        MemberRequest.Login request = MemberRequest.Login.of(username, password);

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-02-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(""))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    private static Stream<Arguments> argsFor_login_BadRequest_InputValidation() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of("user1", ""),
                Arguments.of("", "1234")
        );
    }

    @Test
    @DisplayName("post:/api/members/login - bad request member not exist, F-01-02-02")
    public void login_BadRequest_MemberNotExist() throws Exception {

        // given
        String username = "abcde";
        String password = "12345";
        MemberRequest.Login request = MemberRequest.Login.of(username, password);

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-02-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(username))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("post:/api/members/login - bad request password not matched, F-01-02-03")
    public void login_BadRequest_PasswordNotMatched() throws Exception {

        // given
        String username = "user1";
        String password = "12345";
        MemberRequest.Login request = MemberRequest.Login.of(username, password);

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-02-03"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(password))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("get:/api/members/logout - ok, S-01-03")
    public void logout_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/logout")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-01-03"))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;

        Member member = this.memberService.getUserByUsername("user1");
        assertThat(member.isLogout()).isTrue();
    }

    @Test
    @DisplayName("get:/api/members/me - ok, S-01-04")
    public void me_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/members/me")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-01-04"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.username").value("user1"))
                .andExpect(jsonPath("data.email").exists())
                .andExpect(jsonPath("data.authorities").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists());
    }

    @Transactional
    @Test
    @DisplayName("patch:/api/members/password - ok, S-01-05")
    public void updatePassword_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        String newPassword = "12345";
        String newPasswordConfirm = "12345";
        MemberRequest.UpdatePassword request = MemberRequest.UpdatePassword.builder()
                .oldPassword(password)
                .newPassword(newPassword)
                .newPasswordConfirm(newPasswordConfirm)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/members/password")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-01-05"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;

        Member member = this.memberService.getUserByUsername(username);
        assertThat(this.passwordEncoder.matches(newPassword, member.getPassword())).isTrue();
    }

    @ParameterizedTest
    @MethodSource("argsFor_updatePassword_BadRequest_NotBlank")
    @DisplayName("patch:/api/members/password - bad request not blank, F-01-05-01")
    public void updatePassword_BadRequest_NotBlank(String oldPassword, String newPassword, String newPasswordConfirm) throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        MemberRequest.UpdatePassword request = MemberRequest.UpdatePassword.builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .newPasswordConfirm(newPasswordConfirm)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/members/password")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-05-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(""))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    private static Stream<Arguments> argsFor_updatePassword_BadRequest_NotBlank() {
        return Stream.of(
                Arguments.of("", "12345", "12345"),
                Arguments.of("1234", "", "12345"),
                Arguments.of("1234", "12345", "")
        );
    }

    @Test
    @DisplayName("patch:/api/members/password - bad request old password not matched, F-01-05-02")
    public void updatePassword_BadRequest_OldPasswordNotMatched() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        String oldPassword = "1111";
        String newPassword = "12345";
        String newPasswordConfirm = "12345";
        MemberRequest.UpdatePassword request = MemberRequest.UpdatePassword.builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .newPasswordConfirm(newPasswordConfirm)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/members/password")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-05-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(oldPassword))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @ParameterizedTest
    @MethodSource("argsFor_updatePassword_BadRequest_NewPasswordNotMatched")
    @DisplayName("patch:/api/members/password - bad request new password not matched, F-01-05-03")
    public void updatePassword_BadRequest_NewPasswordNotMatched(String newPassword, String newPasswordConfirm) throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        MemberRequest.UpdatePassword request = MemberRequest.UpdatePassword.builder()
                .oldPassword(password)
                .newPassword(newPassword)
                .newPasswordConfirm(newPasswordConfirm)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/members/password")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-05-03"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(newPasswordConfirm))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    private static Stream<Arguments> argsFor_updatePassword_BadRequest_NewPasswordNotMatched() {
        return Stream.of(
                Arguments.of("12345", "123456"),
                Arguments.of("123456", "12345")
        );
    }

    @Test
    @DisplayName("post:/api/members/username - ok, S-01-06")
    public void findUsername_OK() throws Exception {

        // given
        String username = "user1";
        String email = "user1@test.com";
        MemberRequest.FindUsername request = MemberRequest.FindUsername.builder()
                .email(email)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-01-06"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.username").value(username))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Test
    @DisplayName("post:/api/members/username - bad request not blank, F-01-06-01")
    public void findUsername_BadRequest_NotBlank() throws Exception {

        // given
        String email = "";
        MemberRequest.FindUsername request = MemberRequest.FindUsername.builder()
                .email(email)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-06-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(""))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("post:/api/members/username - bad request member not exist, F-01-06-02")
    public void findUsername_BadRequest_MemberNotExist() throws Exception {

        // given
        String email = "test@test.com";
        MemberRequest.FindUsername request = MemberRequest.FindUsername.builder()
                .email(email)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-06-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(email))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Transactional
    @Test
    @DisplayName("post:/api/members/password - ok, S-01-07")
    public void findPassword_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String email = "user1@test.com";
        MemberRequest.FindPassword request = MemberRequest.FindPassword.builder()
                .username(username)
                .email(email)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-01-07"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;

        Member member = this.memberService.getUserByUsername(username);
        assertThat(this.passwordEncoder.matches(password, member.getPassword())).isFalse();
    }

    @ParameterizedTest
    @MethodSource("argsFor_findPassword_BadRequest_NotBlank")
    @DisplayName("post:/api/members/password - bad request not blank, F-01-07-01")
    public void findPassword_BadRequest_NotBlank(String username, String email) throws Exception {

        // given
        MemberRequest.FindPassword request = MemberRequest.FindPassword.builder()
                .username(username)
                .email(email)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-07-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(""))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    private static Stream<Arguments> argsFor_findPassword_BadRequest_NotBlank() {
        return Stream.of(
                Arguments.of("", "user1@test.com"),
                Arguments.of("user1", "")
        );
    }

    @Test
    @DisplayName("post:/api/members/password - bad request member not exist wrong all, F-01-07-02")
    public void findPassword_BadRequest_MemberNotExist_WrongAll() throws Exception {

        // given
        String username = "test";
        String email = "test@test.com";
        MemberRequest.FindPassword request = MemberRequest.FindPassword.builder()
                .username(username)
                .email(email)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-07-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(username))
                .andExpect(jsonPath("data[1].rejectedValue").value(email))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("post:/api/members/password - bad request member not exist wrong username, F-01-07-02")
    public void findPassword_BadRequest_MemberNotExist_WrongUserName() throws Exception {

        // given
        String username = "test";
        String email = "user1@test.com";
        MemberRequest.FindPassword request = MemberRequest.FindPassword.builder()
                .username(username)
                .email(email)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-07-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(username))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("post:/api/members/password - bad request member not exist wrong email, F-01-07-02")
    public void findPassword_BadRequest_MemberNotExist_WrongEmail() throws Exception {

        // given
        String username = "user1";
        String email = "test@test.com";
        MemberRequest.FindPassword request = MemberRequest.FindPassword.builder()
                .username(username)
                .email(email)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-07-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(email))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }
}