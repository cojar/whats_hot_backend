package com.cojar.whats_hot_backend.domain.comment_module.comment.controller;

import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;

import com.cojar.whats_hot_backend.global.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest extends BaseControllerTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private JwtProvider jwtProvider;

  @Test
  @Transactional
  @DisplayName("POST /api/comments")
  void createComment() throws Exception {

    // given
    String username = "user1";
    String password = "1234";
    String accessToken = "Bearer " + this.memberService.getAccessToken(loginReq.of(username, password));

    // when
    ResultActions resultActions = mockMvc
        .perform(
            post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .content("""
                        {
                        "reviewId": 1,
                        "content": "댓글내용2",
                        "tagId": null
                        }
                        """
                )
        )
        .andDo(print());

    // then
        resultActions
            .andExpect(status().isCreated())
            .andExpect(handler().handlerType(CommentController.class))
            .andExpect(handler().methodName("createComment"))
            .andExpect(jsonPath("$.data.id", is(2)))
            .andExpect(jsonPath("$.data.createDate").exists())
            .andExpect(jsonPath("$.data.modifyDate").exists())
            .andExpect(jsonPath("$.data.author", is("user1")))
            .andExpect(jsonPath("$.data.content", is("댓글내용2")))
            .andExpect(jsonPath("$.data.liked", is(0)));

  }

}