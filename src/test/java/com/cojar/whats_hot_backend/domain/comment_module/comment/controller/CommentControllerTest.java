package com.cojar.whats_hot_backend.domain.comment_module.comment.controller;

import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.repository.CommentRepository;
import com.cojar.whats_hot_backend.domain.comment_module.comment.request.CommentRequest;
import com.cojar.whats_hot_backend.domain.comment_module.comment.service.CommentService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    public void createComment_CREATED(String content, Long reviewId, Long tagId) throws Exception {

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
                .perform(post("/api/comments")
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
    public void createComment_BadRequest_NotBlank(String content, Long reviewId) throws Exception {

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
                .perform(post("/api/comments")
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
    public void createComment_BadRequest_ReviewNotExist() throws Exception {

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
                .perform(post("/api/comments")
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
    public void createComment_BadRequest_TagNotExist() throws Exception {

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
                .perform(post("/api/comments")
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

    @Test
    @DisplayName("post:/api/comments - bad request tag not include in review, F-04-01-04")
    public void createComment_BadRequest_TagNotIncludeInReview() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        List<Long> checkList = getCheckListNotCreated();

        String content = "테스트 댓글";
        Long reviewId = 1L;
        Long tagId = 2L;
        CommentRequest.CreateComment request = CommentRequest.CreateComment.builder()
                .content(content)
                .reviewId(reviewId)
                .tagId(tagId)
                .build();

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/api/comments")
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
                .andExpect(jsonPath("code").value("F-04-01-04"))
                .andExpect(jsonPath("message").value(ResCode.F_04_01_04.getMessage()))
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
    @DisplayName("get:/api/comments/{id} - ok, S-04-02")
    public void getComment_OK() throws Exception {

        // given
        Long id = 1L;
        Comment comment = this.commentService.getCommentById(id);

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/comments/%s".formatted(id))
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-04-02"))
                .andExpect(jsonPath("message").value(ResCode.S_04_02.getMessage()))
                .andExpect(jsonPath("data.id").value(id))
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.content").exists())
                .andExpect(jsonPath("data.liked").exists())
                .andExpect(jsonPath("data.author").exists())
                .andExpect(jsonPath("data.reviewId").exists())
        ;
        if (comment.getTag() != null) resultActions.andExpect(jsonPath("data.tagId").exists());
    }

    @Test
    @DisplayName("get:/api/comments/{id} - bad request not exist, F-04-02-01")
    public void getComments_BadRequest_NotExist() throws Exception {

        // given
        Long id = 100000000L;

        // when
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/comments/%s".formatted(id))
                                .contentType(MediaType.ALL)
                                .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-02-01"))
                .andExpect(jsonPath("message").value(ResCode.F_04_02_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(id))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @ParameterizedTest
    @MethodSource("argsFor_getMyComments_OK")
    @DisplayName("get:/api/comments/me - ok, S-04-03")
    public void getMyComments_OK(int page, int size, String sort) throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (page != 0) params.add("page", page + "");
        if (size != 0) params.add("size", size + "");
        if (!sort.isBlank()) params.add("sort", sort);

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/comments/me")
                        .header("Authorization", accessToken)
                        .params(params)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-04-03"))
                .andExpect(jsonPath("message").value(ResCode.S_04_03.getMessage()))
                .andExpect(jsonPath("data.list[0].id").exists())
                .andExpect(jsonPath("data.list[0].createDate").exists())
                .andExpect(jsonPath("data.list[0].modifyDate").exists())
                .andExpect(jsonPath("data.list[0].content").exists())
                .andExpect(jsonPath("data.list[0].liked").exists())
                .andExpect(jsonPath("data.list[0].author").exists())
                .andExpect(jsonPath("data.list[0].reviewId").exists())
                .andExpect(jsonPath("data.list[0]._links.self").exists())
        ;
        if (sort.equals("new") || sort.isBlank()) {
            resultActions
                    .andExpect(jsonPath("data.sort[0].property").value("createDate"))
                    .andExpect(jsonPath("data.sort[0].direction").value("desc"))
            ;
        } else if (sort.equals("old")) {
            resultActions
                    .andExpect(jsonPath("data.sort[0].property").value("createDate"))
                    .andExpect(jsonPath("data.sort[0].direction").value("asc"))
            ;
        } else if (sort.equals("like")) {
            resultActions
                    .andExpect(jsonPath("data.sort[0].property").value("liked"))
                    .andExpect(jsonPath("data.sort[0].direction").value("desc"))
                    .andExpect(jsonPath("data.sort[1].property").value("createDate"))
                    .andExpect(jsonPath("data.sort[1].direction").value("desc"))
            ;
        }
    }

    private static Stream<Arguments> argsFor_getMyComments_OK() {
        return Stream.of(
                Arguments.of(0, 0, ""),
                Arguments.of(1, 0, ""),
                Arguments.of(0, 20, ""),
                Arguments.of(0, 50, ""),
                Arguments.of(0, 100, ""),
                Arguments.of(1, 20, ""),
                Arguments.of(1, 50, ""),
                Arguments.of(1, 100, ""),
                Arguments.of(1, 0, "new"),
                Arguments.of(0, 20, "new"),
                Arguments.of(0, 50, "new"),
                Arguments.of(0, 100, "new"),
                Arguments.of(1, 20, "new"),
                Arguments.of(1, 50, "new"),
                Arguments.of(1, 100, "new"),
                Arguments.of(1, 0, "old"),
                Arguments.of(0, 20, "old"),
                Arguments.of(0, 50, "old"),
                Arguments.of(0, 100, "old"),
                Arguments.of(1, 20, "old"),
                Arguments.of(1, 50, "old"),
                Arguments.of(1, 100, "old"),
                Arguments.of(1, 0, "like"),
                Arguments.of(0, 20, "like"),
                Arguments.of(0, 50, "like"),
                Arguments.of(0, 100, "like"),
                Arguments.of(1, 20, "like"),
                Arguments.of(1, 50, "like"),
                Arguments.of(1, 100, "like")
        );
    }

    @Test
    @DisplayName("get:/api/comments/me - bad request not exist, F-04-03-01")
    public void getMyComments_BadRequest_NotExist() throws Exception {

        // given
        String username = "user2";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/comments/me")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-03-01"))
                .andExpect(jsonPath("message").value(ResCode.F_04_03_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(username))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("get:/api/comments/me - bad request size not allowed, F-04-03-02")
    public void getMyComments_BadRequest_SizeNotAllowed() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        int size = 22;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("size", size + "");

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/comments/me")
                        .header("Authorization", accessToken)
                        .params(params)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-03-02"))
                .andExpect(jsonPath("message").value(ResCode.F_04_03_02.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(size))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @ParameterizedTest
    @MethodSource("argsFor_getMyComments_BadRequest_PageNotExist")
    @DisplayName("get:/api/comments/me - bad request page not exist, F-04-03-03")
    public void getMyComments_BadRequest_PageNotExist(int size) throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        int page = 100000;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", page + "");
        if (size != 0) params.add("size", size + "");

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/comments/me")
                        .header("Authorization", accessToken)
                        .params(params)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-03-03"))
                .andExpect(jsonPath("message").value(ResCode.F_04_03_03.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(page))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    private static Stream<Arguments> argsFor_getMyComments_BadRequest_PageNotExist() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(20),
                Arguments.of(50),
                Arguments.of(100)
        );
    }

    @Test
    @DisplayName("get:/api/comments/me - bad request sort not allowed, F-04-03-04")
    public void getMyComments_BadRequest_SortNotAllowed() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        String sort = "qwerty";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("sort", sort);

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/comments/me")
                        .header("Authorization", accessToken)
                        .params(params)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-03-04"))
                .andExpect(jsonPath("message").value(ResCode.F_04_03_04.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(sort))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Transactional
    @Test
    @DisplayName("patch:/api/comments/{id} - ok, S-04-04")
    public void updateComment_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        String content = "수정 테스트 댓글";
        CommentRequest.UpdateComment request = CommentRequest.UpdateComment.builder()
                .content(content)
                .build();

        // when
        ResultActions resultActions = mockMvc
                .perform(patch("/api/comments/%s".formatted(id))
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
                .andExpect(jsonPath("code").value("S-04-04"))
                .andExpect(jsonPath("message").value(ResCode.S_04_04.getMessage()))
                .andExpect(jsonPath("data.id").value(id))
                .andExpect(jsonPath("data.content").value(content))
                .andExpect(jsonPath("data.author").value(username))
        ;
    }

    @Test
    @DisplayName("patch:/api/comments/{id} - bad request not exist, F-04-04-01")
    public void updateComment_BadRequest_NotExist() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 10000000L;
        String content = "수정 테스트 댓글";
        CommentRequest.UpdateComment request = CommentRequest.UpdateComment.builder()
                .content(content)
                .build();

        // when
        ResultActions resultActions = mockMvc
                .perform(patch("/api/comments/%s".formatted(id))
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
                .andExpect(jsonPath("code").value("F-04-04-01"))
                .andExpect(jsonPath("message").value(ResCode.F_04_04_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(id))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("patch:/api/comments/{id} - bad request not blank, F-04-04-02")
    public void updateComment_BadRequest_NotBlank() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Comment before = this.commentService.getCommentById(id);
        String content = "";
        CommentRequest.UpdateComment request = CommentRequest.UpdateComment.builder()
                .content(content)
                .build();

        // when
        ResultActions resultActions = mockMvc
                .perform(patch("/api/comments/%s".formatted(id))
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
                .andExpect(jsonPath("code").value("F-04-04-02"))
                .andExpect(jsonPath("message").value(ResCode.F_04_04_02.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(""))
                .andExpect(jsonPath("_links.index").exists())
        ;

        Comment after = this.commentService.getCommentById(id);
        checkNotUpdated(before, after);
    }

    @Test
    @DisplayName("patch:/api/comments/{id} - bad request not allowed, F-04-04-03")
    public void updateComment_BadRequest_NotAllowed() throws Exception {

        // given
        String username = "user2";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Comment before = this.commentService.getCommentById(id);
        String content = "수정 테스트 댓글";
        CommentRequest.UpdateComment request = CommentRequest.UpdateComment.builder()
                .content(content)
                .build();

        // when
        ResultActions resultActions = mockMvc
                .perform(patch("/api/comments/%s".formatted(id))
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
                .andExpect(jsonPath("code").value("F-04-04-03"))
                .andExpect(jsonPath("message").value(ResCode.F_04_04_03.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(username))
                .andExpect(jsonPath("_links.index").exists())
        ;

        Comment after = this.commentService.getCommentById(id);
        checkNotUpdated(before, after);
    }

    private void checkNotUpdated(Comment before, Comment after) {
        assertThat(before.getContent()).isEqualTo(after.getContent());
    }

    @Transactional
    @Test
    @DisplayName("delete:/api/comments/{id} - ok, S-04-05")
    public void deleteComment_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(delete("/api/comments/%s".formatted(id))
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
                .andExpect(jsonPath("code").value("S-04-05"))
                .andExpect(jsonPath("message").value(ResCode.S_04_05.getMessage()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;

        ResData resData = assertThrows(ApiResponseException.class, () -> this.commentService.getCommentById(id)).getResData();
        assertThat(resData).isNotNull();
        assertThat(resData.getCode()).isEqualTo(ResCode.F_04_02_01.getCode());
    }

    @Test
    @DisplayName("delete:/api/comments/{id} - bad request not exist, F-04-05-01")
    public void deleteComment_BadRequest_NotExist() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1000000L;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(delete("/api/comments/%s".formatted(id))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-05-01"))
                .andExpect(jsonPath("message").value(ResCode.F_04_05_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(id))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("delete:/api/comments/{id} - bad request not allowed, F-04-05-02")
    public void deleteComment_BadRequest_NotAllowed() throws Exception {

        // given
        String username = "user2";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(delete("/api/comments/%s".formatted(id))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-05-02"))
                .andExpect(jsonPath("message").value(ResCode.F_04_05_02.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(username))
                .andExpect(jsonPath("_links.index").exists())
        ;

        assertDoesNotThrow(() -> this.commentService.getCommentById(id));
    }

    @Transactional
    @Test
    @DisplayName("patch:/api/comments/{id}/like - ok like, S-04-06")
    public void likeComment_OK_Like() throws Exception {

        // given
        String username = "user2";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Long liked = this.commentService.getCommentById(id).getLiked();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/comments/%s/like".formatted(id))
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
                .andExpect(jsonPath("code").value("S-04-06"))
                .andExpect(jsonPath("message").value(ResCode.S_04_06.getMessage()))
                .andExpect(jsonPath("data.liked").value(liked + 1))
                .andExpect(jsonPath("data.like").value(true))
        ;
    }

    @Transactional
    @Test
    @DisplayName("patch:/api/comments/{id}/like - ok undo like , S-04-06")
    public void likeComment_OK_UndoLike() throws Exception {

        // given
        String username = "user2";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Comment comment = this.commentService.getCommentById(1L).toBuilder()
                .liked(1L)
                .likedMember(Set.of(this.memberService.getUserByUsername(username)))
                .build();
        this.commentRepository.save(comment);
        Long liked = comment.getLiked();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/comments/%s/like".formatted(id))
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
                .andExpect(jsonPath("code").value("S-04-06"))
                .andExpect(jsonPath("message").value(ResCode.S_04_06.getMessage()))
                .andExpect(jsonPath("data.liked").value(liked - 1))
                .andExpect(jsonPath("data.like").value(false))
        ;
    }

    @Test
    @DisplayName("patch:/api/comments/{id}/like - bad request not exist, F-04-06-01")
    public void likeComment_BadRequest_NotExist() throws Exception {

        // given
        String username = "user2";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1000000L;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/comments/%s/like".formatted(id))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-06-01"))
                .andExpect(jsonPath("message").value(ResCode.F_04_06_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(id))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("patch:/api/comments/{id}/like - bad request not allowed, F-04-06-02")
    public void likeComment_BadRequest_NotAllowed() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Comment before = this.commentService.getCommentById(id);

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/comments/%s/like".formatted(id))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-04-06-02"))
                .andExpect(jsonPath("message").value(ResCode.F_04_06_02.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(username))
                .andExpect(jsonPath("_links.index").exists())
        ;

        Comment after = this.commentService.getCommentById(id);
        checkNotLiked(before, after);
    }

    private void checkNotLiked(Comment before, Comment after) {
        assertThat(before.getLiked()).isEqualTo(after.getLiked());
    }
}
