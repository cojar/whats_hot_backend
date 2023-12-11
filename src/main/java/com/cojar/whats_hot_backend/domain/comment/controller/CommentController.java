package com.cojar.whats_hot_backend.domain.comment.controller;

import com.cojar.whats_hot_backend.domain.comment.api_response.CommentApiResponse;
import com.cojar.whats_hot_backend.domain.comment.dto.CommentDto;
import com.cojar.whats_hot_backend.domain.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment.request.CommentRequest;
import com.cojar.whats_hot_backend.domain.comment.service.CommentService;
import com.cojar.whats_hot_backend.domain.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.spot.controller.SpotController;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Tag(name = "Comments", description = "댓글 서비스 API")
@RequestMapping(value = "/api/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @CommentApiResponse.Create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createComment(@Valid @RequestPart(value = "request") CommentRequest.CreateComment request, Errors errors,
                                        @AuthenticationPrincipal User user) {

        Member author = this.memberService.getUserByUsername(user.getUsername());

        Comment comment = this.commentService.getCommentById(1L);

        ResData resData = ResData.of(
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
}
