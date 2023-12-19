package com.cojar.whats_hot_backend.domain.comment_module.comment.service;

import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.repository.CommentRepository;
import com.cojar.whats_hot_backend.domain.comment_module.comment.request.CommentRequest;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

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

        if (errors.hasErrors()){
            return ResData.of(
                HttpStatus.BAD_REQUEST,
                "F-04-01-02",
                "댓글을 작성해주십시오.",
                errors
            );
        }

        Review review = this.reviewService.getReviewById(request.getReviewId());

        if (review == null) {
            errors.rejectValue("reviewId", "not exist", "존재하지 않는 리뷰입니다.");

            return ResData.of(
                HttpStatus.BAD_REQUEST,
                "F-04-01-01",
                "존재하지 않는 리뷰입니다."
            );
        }
        return null;
    }
}
