package com.cojar.whats_hot_backend.domain.member_module.member.controller;

import com.cojar.whats_hot_backend.domain.member_module.member.request.MemberRequest;
import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends BaseControllerTest {

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
        String username = "tester";
        String password = "1234";
        String passwordConfirm = "1235";
        String email = "tester@test.io";

        // given
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
    }

    @Test
    @DisplayName("post:/api/members - bad request username unique violation, F-01-01-03")
    public void signup_BadRequest_UsernameUniqueViolation() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String passwordConfirm = "1234";
        String email = "tester@test.io";

        // given
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
    }

    @Test
    @DisplayName("post:/api/members - bad request email unique violation, F-01-01-04")
    public void signup_BadRequest_EmailUniqueViolation() throws Exception {

        // given
        String username = "tester";
        String password = "1234";
        String passwordConfirm = "1234";
        String email = "user1@test.com";

        // given
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
    }

    @Test
    @DisplayName("post:/api/members/login - ok, S-01-02")
    public void login_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "%s",
                                    "password": "%s"
                                }
                                """.formatted(username, password).stripIndent())
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

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "%s",
                                    "password": "%s"
                                }
                                """.formatted(username, password).stripIndent())
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

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "%s",
                                    "password": "%s"
                                }
                                """.formatted(username, password).stripIndent())
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

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "%s",
                                    "password": "%s"
                                }
                                """.formatted(username, password).stripIndent())
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
    @DisplayName("get:/api/members/me - ok, S-01-04")
    public void me_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = "Bearer " + this.memberService.getAccessToken(loginReq.of(username, password));

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
                .andExpect(jsonPath("_links.profile").exists())
        ;
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
}