package com.cojar.whats_hot_backend.domain.comment_module.comment.service;

import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.repository.CommentRepository;
import com.cojar.whats_hot_backend.domain.comment_module.comment.request.CommentRequest;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
<<<<<<< HEAD
=======
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
>>>>>>> b3f824a (validation service로 이전)
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

<<<<<<< HEAD
=======
    private final MemberService memberService;

>>>>>>> b3f824a (validation service로 이전)
    private final ReviewService reviewService;

    public Comment getCommentById(Long id) {
        return this.commentRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public Comment create(Member author, Review review, String content, Comment tag) {

        Comment comment = Comment.builder()
                .author(author)
                .review(review)
                .content(content)
                .tag(tag != null ? tag : null)
                .build();

        this.commentRepository.save(comment);

        return comment;
    }

    public ResData createValidate(CommentRequest.CreateComment request, Errors errors) {

<<<<<<< HEAD
        if (errors.hasErrors()){
            return ResData.of(
                HttpStatus.BAD_REQUEST,
                "F-04-01-02",
                "댓글을 작성해주십시오.",
                errors
            );
        }

=======
>>>>>>> b3f824a (validation service로 이전)
        Review review = this.reviewService.getReviewById(request.getReviewId());

        if (review == null) {
            errors.rejectValue("reviewId", "not exist", "존재하지 않는 리뷰입니다.");

            return ResData.of(
                HttpStatus.BAD_REQUEST,
                "F-04-01-01",
                "존재하지 않는 리뷰입니다."
            );
        }
<<<<<<< HEAD
=======

        if (request.getContent().equals("") || request.getContent().trim().isEmpty()){
            errors.rejectValue("content", "not exist", "댓글 내용을 작성해주세요.");

           return ResData.of(
                HttpStatus.BAD_REQUEST,
                "F-04-01-02",
                "댓글 내용이 없습니다.",
                errors
            );
        }
>>>>>>> b3f824a (validation service로 이전)
        return null;
    }
}
