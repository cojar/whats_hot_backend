package com.cojar.whats_hot_backend.domain.comment_module.comment.service;

import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.repository.CommentRepository;
import com.cojar.whats_hot_backend.domain.comment_module.comment.request.CommentRequest;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
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

        if (errors.hasErrors()) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_01_02,
                            errors
                    )
            );
        }

        Review review = this.reviewService.getReviewById(request.getReviewId());

        if (review == null) {
            errors.rejectValue("reviewId", "not exist", "존재하지 않는 리뷰입니다.");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_01_01
                    )
            );
        }
        return null;
    }

    public ResData getValidate(Long commentId) {

        Errors errors = new BeanPropertyBindingResult(null,"comment");

        errors.reject("not exist", new Object[]{commentId}, "Comment that has id does not exist");


        if (!this.commentRepository.existsById(commentId)) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_02_01,
                        errors
                    )
            );
        }
        return null;
    }


    public ResData getMyCommentsValidate(Member author) {

        Errors errors = new BeanPropertyBindingResult(null,"comment");

        errors.reject("not exist", new Object[]{author.getId()}, "Author's comments does not exist");

        if (this.commentRepository.countByAuthor(author) == 0) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_03_01,
                        errors
                    )
            );
        }
        return null;
    }

    public ResData updateValidate(User user, Comment comment, Errors errors) {
        if (errors.hasErrors()) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_04_01,
                            errors
                    )
            );
        }

        if (comment == null) {

            errors.reject("not exist", new Object[]{"NULL"}, "Comment that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_04_02,
                        errors
                    )
            );
        }

        if (!comment.getAuthor().getUsername().equals(user.getUsername())) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_04_03,
                            errors
                    )
            );
        }

        return null;
    }

    public ResData deleteValidate(User user, Comment comment) {

        if (comment == null) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_05_01
                    )
            );
        }

        if (!comment.getAuthor().getUsername().equals(user.getUsername())) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_05_02
                    )
            );
        }

        return null;
    }

    public ResData likeValidate(User user, Comment comment) {

        if (comment == null) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_06_01
                    )
            );
        }

        if (comment.getAuthor().getUsername().equals(user.getUsername())) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_06_02
                    )
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

    @Transactional
    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

    public List<Comment> getAllByAuthor(Member author) {
        return this.commentRepository.findAllByAuthor(author);
    }


    @Transactional
    public void toggleLike(Comment comment, Member user) {

        if (comment.getLikedMember().contains(user)) {

            comment = comment.toBuilder()
                    .liked(comment.getLiked() - 1)
                    .build();
            comment.getLikedMember().remove(user);
            this.commentRepository.save(comment);

        } else {

            comment = comment.toBuilder()
                    .liked(comment.getLiked() + 1)
                    .build();
            comment.getLikedMember().add(user);
            this.commentRepository.save(comment);

        }
    }

}
