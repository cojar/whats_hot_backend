package com.cojar.whats_hot_backend.domain.member_module.member.controller;

import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends BaseControllerTest {

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
}