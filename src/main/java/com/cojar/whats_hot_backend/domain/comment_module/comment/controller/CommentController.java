package com.cojar.whats_hot_backend.domain.comment_module.comment.controller;

import com.cojar.whats_hot_backend.domain.comment_module.comment.api_response.CommentApiResponse;
import com.cojar.whats_hot_backend.domain.comment_module.comment.dto.CommentDto;
import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.request.CommentRequest;
import com.cojar.whats_hot_backend.domain.comment_module.comment.service.CommentService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.controller.SpotController;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @PostMapping(value = "",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createComment(@Valid @RequestBody CommentRequest.CreateComment request,
                                        Errors errors,
                                        @AuthenticationPrincipal User user) {

        ResData resData = this.commentService.createValidate(request, errors);

        if (resData != null) return ResponseEntity.badRequest().body(resData);

        Member author = this.memberService.getUserByUsername(user.getUsername());

        Review review = this.reviewService.getReviewById(request.getReviewId());

        Comment tag = (request.getTagId() != null) ? this.commentService.getCommentById(request.getTagId()) : null;

        Comment comment = this.commentService.create(author, review, request.getContent(), tag);

        resData = ResData.of(
            HttpStatus.CREATED,
            "S-04-01",
            "댓글 등록이 완료되었습니다",
            CommentDto.of(comment),
            linkTo(SpotController.class).slash(comment.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Comment/createComment").withRel("profile"));
        return ResponseEntity.created(resData.getSelfUri())
            .body(resData);
    }

    @CommentApiResponse.Detail
    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity getComment(@PathVariable(value = "id") Long id) {

        ResData resData = this.commentService.getValidate(id);

        if (resData != null) return ResponseEntity.badRequest().body(resData);

        Comment comment = this.commentService.getCommentById(id);

        resData = ResData.of(
            HttpStatus.OK,
            "S-04-02",
            "요청하신 댓글 정보를 반환합니다",
            CommentDto.of(comment),
            linkTo(this.getClass()).slash(comment.getId())
        );

        return ResponseEntity.ok()
            .body(resData);
    }


    @CommentApiResponse.Me
    @GetMapping(value = "/me", consumes = MediaType.ALL_VALUE)
    public ResponseEntity getMyComments(@AuthenticationPrincipal User user) {

        Member author = this.memberService.getUserByUsername(user.getUsername());

        List<Comment> comments = this.commentService.getAllByAuthor(author);

        ResData resData = this.commentService.getMyCommentsValidate(author);

        if (resData != null) return ResponseEntity.badRequest().body(resData);

        List<CommentDto> commentDtos = comments.stream()
            .map(CommentDto::of)
            .collect(Collectors.toList());

        resData = ResData.of(
            HttpStatus.OK,
            "S-04-03",
            "요청하신 댓글 리스트입니다.",
            commentDtos
        );

        return ResponseEntity.ok().body(resData);

    }

    @CommentApiResponse.Update
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateComment(@Valid @RequestBody CommentRequest.UpdateComment request, Errors errors,
                                        @PathVariable(value = "id") Long id,
                                        @AuthenticationPrincipal User user) {

        Comment comment = this.commentService.getCommentById(id);

        ResData resData = this.commentService.updateValidate(user, comment, errors);

        if (resData != null) return ResponseEntity.badRequest().body(resData);

        this.commentService.update(comment, request.getContent());

        resData = ResData.of(
            HttpStatus.OK,
            "S-04-04",
            "댓글 수정이 완료되었습니다",
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

        Comment comment = this.commentService.getCommentById(id);

        ResData resData = this.commentService.deleteValidate(user, comment);

        if (resData != null) return ResponseEntity.badRequest().body(resData);

        this.commentService.delete(comment);

        resData = ResData.of(
                HttpStatus.OK,
                "S-04-05",
                "댓글 삭제가 완료되었습니다"
        );

        return ResponseEntity.ok()
                .body(resData);
    }

    @CommentApiResponse.Like
    @PatchMapping(value = "/{id}/like", consumes = MediaType.ALL_VALUE)
    public ResponseEntity likeComment(@PathVariable(value = "id") Long id,
                                      @AuthenticationPrincipal User user) {

        Comment comment = this.commentService.getCommentById(id);

        ResData resData = ResData.of(
                HttpStatus.OK,
                "S-04-05",
                "댓글 좋아요 상태가 변경되었습니다",
                CommentDto.of(comment),
                linkTo(this.getClass()).slash(comment.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Comment/likeComment").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }
}
