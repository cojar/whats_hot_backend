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
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.List;

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

    public ResData getValidate(Long commentId) {

        if (!this.commentRepository.existsById(commentId)) {
            return ResData.of(
                HttpStatus.BAD_REQUEST,
                "F-04-02-01",
                "존재하지 않는 댓글입니다."
            );
        }
        return null;
    }


    public ResData getMyCommentsValidate(Member author) {

        if (this.commentRepository.countByAuthor(author) == 0) {
            return ResData.of(
                HttpStatus.BAD_REQUEST,
                "F-04-03-01",
                "작성한 댓글이 없습니다."
            );
        }
        return null;
    }

    public ResData updateValidate(User user, Comment comment, Errors errors) {
        if (errors.hasErrors()) {
            return ResData.of(
                HttpStatus.BAD_REQUEST,
                "F-04-04-01",
                "올바르지 않은 입력값입니다.",
                errors
            );
        }

        if (comment == null){

            return ResData.of(
                HttpStatus.BAD_REQUEST,
                "F-04-04-02",
                "존재하지 않는 댓글입니다."
            );
        }

        if (!comment.getAuthor().getUsername().equals(user.getUsername())){

            return ResData.of(
                HttpStatus.BAD_REQUEST,
                "F-04-04-03",
                "수정 권한이 없습니다.",
                errors
            );
        }

        return null;
    }

    @Transactional
    public void update(Comment comment, String content) {
        comment = comment.toBuilder()
            .content(content)
            .modifyDate(LocalDateTime.now())
            .build();
        this.commentRepository.save(comment);
    }


    public List<Comment> getAllByAuthor(Member author) {
        return this.commentRepository.findAllByAuthor(author);
    }
}
