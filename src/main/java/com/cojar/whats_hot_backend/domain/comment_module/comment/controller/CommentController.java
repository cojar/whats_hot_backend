package com.cojar.whats_hot_backend.domain.comment_module.comment.controller;

import com.cojar.whats_hot_backend.domain.comment_module.comment.api_response.CommentApiResponse;
import com.cojar.whats_hot_backend.domain.comment_module.comment.dto.CommentDto;
import com.cojar.whats_hot_backend.domain.comment_module.comment.dto.CommentLikeDto;
import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.request.CommentRequest;
import com.cojar.whats_hot_backend.domain.comment_module.comment.service.CommentService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.PagedDataModel;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Tag(name = "Comments", description = "댓글 서비스 API")
@RequestMapping(value = "/api/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    @CommentApiResponse.Create
    @PostMapping
    public ResponseEntity createComment(@Valid @RequestBody CommentRequest.CreateComment request, Errors errors,
                                        @AuthenticationPrincipal User user) {

        Comment comment = this.commentService.create(request, errors, user);

        ResData resData = ResData.of(
                ResCode.S_04_01,
                CommentDto.of(comment),
                linkTo(this.getClass()).slash(comment.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Comment/createComment").withRel("profile"));

        return ResponseEntity.created(resData.getSelfUri())
                .body(resData);
    }

    @CommentApiResponse.Detail
    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity getComment(@PathVariable(value = "id") Long id) {

        Comment comment = this.commentService.getCommentById(id);

        ResData resData = ResData.of(
                ResCode.S_04_02,
                CommentDto.of(comment),
                linkTo(this.getClass()).slash(comment.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Comment/getComment").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }

    @CommentApiResponse.Me
    @GetMapping(value = "/me", consumes = MediaType.ALL_VALUE)
    public ResponseEntity getMyComments(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "20") int size,
                                        @RequestParam(value = "sort", defaultValue = "new") String sort,
                                        @AuthenticationPrincipal User user) {

        Member member = this.memberService.getUserByUsername(user.getUsername());
        Page<DataModel> commentPaging = this.commentService.getPagingByAuthor(page, size, sort, member);

        ResData resData = ResData.of(
                ResCode.S_04_03,
                PagedDataModel.of(commentPaging)
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Comment/getMyComments").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }

    @CommentApiResponse.Update
    @PatchMapping(value = "/{id}")
    public ResponseEntity updateComment(@Valid @RequestBody CommentRequest.UpdateComment request, Errors errors,
                                        @PathVariable(value = "id") Long id,
                                        @AuthenticationPrincipal User user) {

        Comment comment = this.commentService.update(request, errors, id, user);

        ResData resData = ResData.of(
                ResCode.S_04_04,
                CommentDto.of(comment),
                linkTo(this.getClass()).slash(comment.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Comment/updateComment").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }


    @CommentApiResponse.Delete
    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity deleteComment(@PathVariable(value = "id") Long id,
                                        @AuthenticationPrincipal User user) {

        this.commentService.delete(id, user);

        ResData resData = ResData.of(
                ResCode.S_04_05,
                linkTo(this.getClass())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Comment/deleteComment").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }

    @CommentApiResponse.Like
    @PatchMapping(value = "/{id}/like", consumes = MediaType.ALL_VALUE)
    public ResponseEntity likeComment(@PathVariable(value = "id") Long id,
                                      @AuthenticationPrincipal User user) {

        Member member = this.memberService.getUserByUsername(user.getUsername());
        Comment comment = this.commentService.toggleLike(id, member);

        ResData resData = ResData.of(
                ResCode.S_04_06,
                CommentLikeDto.of(comment, member),
                linkTo(this.getClass()).slash(comment.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Comment/likeComment").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }
}
