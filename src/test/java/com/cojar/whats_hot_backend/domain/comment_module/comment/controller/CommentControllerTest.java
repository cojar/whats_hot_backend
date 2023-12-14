package com.cojar.whats_hot_backend.domain.comment_module.comment.controller;

import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;

import com.cojar.whats_hot_backend.global.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest extends BaseControllerTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private JwtProvider jwtProvider;

  @Test
  @DisplayName("POST /api/comments")
  void createComment() throws Exception {

    // given
    Member member = memberService.getUserByUsername("user1");

    Review review = reviewService.getReviewById(2L);

    Comment comment = Comment.builder()
        .author(member)
        .review(review)
        .content("댓글3")
        .tag(null)
        .build();

    Map<String, Object> claims = new HashMap<>();
    claims.put("id", member.getId());

    String jwtToken = jwtProvider.genToken(claims, 1000);

    // when
    ResultActions resultActions = mockMvc
        .perform(
            post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .content("""
                        {
                        "reviewId": 3,
                        "content": "댓글3",
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
            .andExpect(jsonPath("$.data.content", is("댓글3")))
            .andExpect(jsonPath("$.data.liked", is(0)));

        boolean tokenValid = jwtProvider.verify(jwtToken);
        assertThat(tokenValid).isTrue();
  }

}