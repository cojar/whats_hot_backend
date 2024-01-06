package com.cojar.whats_hot_backend.domain.comment_module.comment.controller;

import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.repository.CommentRepository;
import com.cojar.whats_hot_backend.domain.comment_module.comment.request.CommentRequest;
import com.cojar.whats_hot_backend.domain.comment_module.comment.service.CommentService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;
import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CommentControllerTest extends BaseControllerTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    @ParameterizedTest
    @MethodSource("argsFor_createComment_CREATED")
    @DisplayName("post:/api/comments - created, S-04-01")
    void createComment_CREATED(String content, Long reviewId, Long tagId) throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        CommentRequest.CreateComment request = CommentRequest.CreateComment.builder()
                .content(content)
                .reviewId(reviewId)
                .tagId(tagId)
                .build();

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        post("/api/comments")
                                .header("Authorization", accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(request))
                                .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-04-01"))
                .andExpect(jsonPath("message").value(ResCode.S_04_01.getMessage()))
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.content").value(content))
                .andExpect(jsonPath("data.liked").value(0L))
                .andExpect(jsonPath("data.author").value(username))
                .andExpect(jsonPath("data.reviewId").value(reviewId))
        ;
        if (tagId != null) resultActions.andExpect(jsonPath("data.tagId").value(tagId));
    }

    private static Stream<Arguments> argsFor_createComment_CREATED() {
        return Stream.of(
                Arguments.of("테스트 댓글", 1L, 1L),
                Arguments.of("테스트 댓글", 1L, null)
        );
    }

    @ParameterizedTest
    @MethodSource("argsFor_createComment_BadRequest_NotBlank")
    @DisplayName("post:/api/comments - bad request not blank, F-04-01-01")
    void createComment_BadRequest_NotBlank(String content, Long reviewId) throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        List<Long> checkList = getCheckListNotCreated();

        CommentRequest.CreateComment request = CommentRequest.CreateComment.builder()
                .content(content)
                .reviewId(reviewId)
                .build();

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        post("/api/comments")
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
                .andExpect(jsonPath("code").value("F-04-01-01"))
                .andExpect(jsonPath("message").value(ResCode.F_04_01_01.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;

        if (reviewId == null) resultActions.andExpect(jsonPath("data[0].rejectedValue").doesNotExist());
        else resultActions.andExpect(jsonPath("data[0].rejectedValue").value(""));

        checkNotCreated(checkList);
    }

    private static Stream<Arguments> argsFor_createComment_BadRequest_NotBlank() {
        return Stream.of(
                Arguments.of("", 1L),
                Arguments.of("테스트 댓글", null)
        );
    }

    @Test
    @DisplayName("post:/api/comments - bad request review not exist, F-04-01-02")
    void createComment_BadRequest_ReviewNotExist() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        List<Long> checkList = getCheckListNotCreated();

        String content = "테스트 댓글";
        Long reviewId = 100000000L;
        CommentRequest.CreateComment request = CommentRequest.CreateComment.builder()
                .content(content)
                .reviewId(reviewId)
                .build();

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        post("/api/comments")
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
                .andExpect(jsonPath("code").value("F-04-01-02"))
                .andExpect(jsonPath("message").value(ResCode.F_04_01_02.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(reviewId))
                .andExpect(jsonPath("_links.index").exists())
        ;

        checkNotCreated(checkList);
    }

    @Test
    @DisplayName("post:/api/comments - bad request tag not exist, F-04-01-03")
    void createComment_BadRequest_TagNotExist() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        List<Long> checkList = getCheckListNotCreated();

        String content = "테스트 댓글";
        Long reviewId = 1L;
        Long tagId = 10000000L;
        CommentRequest.CreateComment request = CommentRequest.CreateComment.builder()
                .content(content)
                .reviewId(reviewId)
                .tagId(tagId)
                .build();

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        post("/api/comments")
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
                .andExpect(jsonPath("code").value("F-04-01-03"))
                .andExpect(jsonPath("message").value(ResCode.F_04_01_03.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(tagId))
                .andExpect(jsonPath("_links.index").exists())
        ;

        checkNotCreated(checkList);
    }

    private List<Long> getCheckListNotCreated() {
        return List.of(
                this.commentService.count() + 1
        );
    }

    private void checkNotCreated(List<Long> checkList) {
        int i = 0;
        ResData resData = assertThrows(ApiResponseException.class, () -> this.commentService.getCommentById(checkList.get(i))).getResData();
        assertThat(resData).isNotNull();
        assertThat(resData.getCode()).isEqualTo(ResCode.F_04_02_01.getCode());
    }

    @Test
    @DisplayName("GET /api/comments/1")
    void getComment_OK() throws Exception {

        // given


        // when
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/comments/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)

                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-04-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.id").value(1))
                .andExpect(jsonPath("data.content").value("댓글내용1"))
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.author").value("user1"));
    }

    @Test
    @DisplayName("GET /api/comments/1")
    void getComments_BadRequest_CommentsNotExist() throws Exception {

        // given


        // when
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/comments/3")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)

                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-02-01"))
                .andExpect(jsonPath("message").exists());
    }

    @Test
    @DisplayName("GET /api/comments/me")
    void getMyComments_OK() throws Exception {

        // given

        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);


        // when
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/comments/me")
                                .header("Authorization", accessToken)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-04-03"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data").isArray())
                .andExpect(jsonPath("data[0].content").value("댓글내용1"))
                .andExpect(jsonPath("data[0].id").value("1"))
                .andExpect(jsonPath("data[0].createDate").exists())
                .andExpect(jsonPath("data[0].modifyDate").exists())
                .andExpect(jsonPath("data[0].author").value("user1"));
    }

    @Test
    @DisplayName("GET /api/comments/me")
    void getMyComments_BadRequest_CommentsNotExist() throws Exception {

        // given

        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);


        // when
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/comments/me")
                                .header("Authorization", accessToken)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-03-01"))
                .andExpect(jsonPath("message").exists());
    }

    @Test
    @DisplayName("PATCH /api/comments/1")
    void updateComment_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        patch("/api/comments/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", accessToken)
                                .content("""
                                        {
                                        "content": "댓글내용10"
                                        }
                                        """
                                )
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-04-04"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.content").value("댓글내용10"));
    }

    @Test
    @DisplayName("PATCH /api/comments/1")
    void updateComment_BadRequest_NotNull() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        String content = "  ";

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        patch("/api/comments/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", accessToken)
                                .content("""
                                        {
                                        "content": "%s"
                                        }
                                        """.formatted(content).stripIndent())
                                .accept(MediaTypes.HAL_JSON)

                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("code").value("F-04-04-01"))
                .andExpect(jsonPath("message").value("내용을 작성해주십시오."))
                .andExpect(jsonPath("data[0].field").value("content"))
                .andExpect(jsonPath("data[0].objectName").value("updateComment"))
                .andExpect(jsonPath("data[0].code").value("NotBlank"))
                .andExpect(jsonPath("data[0].defaultMessage").value("must not be blank"))
                .andExpect(jsonPath("data[0].rejectedValue").value("  "));

    }

    @Test
    @DisplayName("PATCH /api/comments/10")
    void updateComment_BadRequest_CommentNotExist() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        String content = "안녕하세요";

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        patch("/api/comments/10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", accessToken)
                                .content("""
                                        {
                                        "content": "%s"
                                        }
                                        """.formatted(content).stripIndent())
                                .accept(MediaTypes.HAL_JSON)

                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-04-02"))
                .andExpect(jsonPath("message").exists());
    }

    @Test
    @DisplayName("PATCH /api/comments/1")
    void updateComment_BadRequest_MismatchedUser() throws Exception {

        // given
        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        String content = "안녕하세요";

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        patch("/api/comments/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", accessToken)
                                .content("""
                                        {
                                        "content": "%s"
                                        }
                                        """.formatted(content).stripIndent())
                                .accept(MediaTypes.HAL_JSON)

                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-04-03"))
                .andExpect(jsonPath("message").exists());
    }

    @Test
    @DisplayName("DELETE /api/comments/1")
    void deleteComment_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        delete("/api/comments/1")
                                .header("Authorization", accessToken)

                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-04-05"))
                .andExpect(jsonPath("message").exists());

        assertThat(commentRepository.findById(1L)).isEmpty();

    }

    @Test
    @DisplayName("DELETE /api/comments/10")
    void deleteComment_BadRequest_CommentNotExist() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        delete("/api/comments/10")
                                .header("Authorization", accessToken)

                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-05-01"))
                .andExpect(jsonPath("message").exists());
    }

    @Test
    @DisplayName("DELETE /api/comments/1")
    void deleteComment_BadRequest_MismatchedUser() throws Exception {

        // given
        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        delete("/api/comments/1")
                                .header("Authorization", accessToken)

                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-05-02"))
                .andExpect(jsonPath("message").exists());
    }

    @Test
    @DisplayName("PATCH /api/comments/{id}/like - Like Comment")
    void likeComment_OK() throws Exception {
        // given
        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        patch("/api/comments/1/like")
                                .header("Authorization", accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-04-06"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.liked").value("1"));
    }

    @Test
    @Transactional
    @DisplayName("PATCH /api/comments/{id}/like - Unlike Comment")
    void unlikeComment_OK() throws Exception {
        // given

        Member member = this.memberService.getUserByUsername("admin");

        Comment comment = this.commentService.getCommentById(1L);

        comment = comment.toBuilder()
                .liked(1L)
                .likedMember(Set.of(member))
                .build();

        this.commentRepository.save(comment);

        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        patch("/api/comments/1/like")
                                .header("Authorization", accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-04-06"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.liked").value("0"));
    }

    @Test
    @DisplayName("PATCH /api/comments/10/like")
    void likeComment_BadRequest_CommentNotExist() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        String content = "안녕하세요";

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        patch("/api/comments/10/like")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", accessToken)

                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-06-01"))
                .andExpect(jsonPath("message").exists());
    }

    @Test
    @DisplayName("PATCH /api/comments/1/like")
    void likeComment_BadRequest_MatchedUser() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        patch("/api/comments/1/like")
                                .header("Authorization", accessToken)

                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-06-02"))
                .andExpect(jsonPath("message").exists());
    }

}
