package com.cojar.whats_hot_backend.domain.comment_module.comment.service;

import com.cojar.whats_hot_backend.domain.comment_module.comment.controller.CommentController;
import com.cojar.whats_hot_backend.domain.comment_module.comment.dto.CommentDto;
import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment_module.comment.repository.CommentRepository;
import com.cojar.whats_hot_backend.domain.comment_module.comment.request.CommentRequest;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.repository.ReviewRepository;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    private final ReviewService reviewService;
    private final MemberService memberService;

    public long count() {
        return this.commentRepository.count();
    }

    @Transactional
    public Comment create(CommentRequest.CreateComment request, Errors errors, User user) {

        this.createValidate(request, errors);

        Comment comment = Comment.builder()
                .author(this.memberService.getUserByUsername(user.getUsername()))
                .content(request.getContent())
                .review(this.reviewService.getReviewById(request.getReviewId()))
                .tag(request.getTagId() != null ? this.getCommentById(request.getTagId()) : null)
                .build();

        this.commentRepository.save(comment);

        return comment;
    }

    public void createValidate(CommentRequest.CreateComment request, Errors errors) {

        if (errors.hasErrors()) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_01_01,
                            errors
                    )
            );
        }

        if (!this.reviewRepository.existsById(request.getReviewId())) {

            errors.rejectValue("reviewId", "not exist", "review that has reviewId does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_01_02,
                            errors
                    )
            );
        }

        if (request.getTagId() != null) {

            if (!this.commentRepository.existsById(request.getTagId())) {

                errors.rejectValue("tagId", "not exist", "comment that has tagId does not exist");

                throw new ApiResponseException(
                        ResData.of(
                                ResCode.F_04_01_03,
                                errors
                        )
                );
            }

            if (this.getCommentById(request.getTagId()).getReview().getId() != request.getReviewId()) {

                errors.rejectValue("tagId", "not include", "comment that has tagId does not include in review");

                throw new ApiResponseException(
                        ResData.of(
                                ResCode.F_04_01_04,
                                errors
                        )
                );
            }
        }
    }

    public Comment getCommentById(Long id) {

        this.getCommentByIdValidate(id);

        return this.commentRepository.findById(id)
                .orElse(null);
    }

    public void getCommentByIdValidate(Long id) {

        Errors errors = AppConfig.getMockErrors("comment");

        if (!this.commentRepository.existsById(id)) {

            errors.reject("not exist", new Object[]{id}, "comment that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_02_01,
                            errors
                    )
            );
        }
    }

    public Page<DataModel> getPagingByAuthor(int page, int size, String sort, Member author) {

        this.getPagingByAuthorValidate(page, size, sort, author);

        List<Sort.Order> sorts = new ArrayList<>();
        if (sort.equals("like")) {
            sorts.add(Sort.Order.desc("liked"));
            sorts.add(Sort.Order.desc("createDate"));
        } else if (sort.equals("old")) {
            sorts.add(Sort.Order.asc("createDate"));
        } else {
            sorts.add(Sort.Order.desc("createDate"));
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sorts));

        return this.commentRepository.findAllByAuthor(author, pageable)
                .map(comment -> {
                    DataModel dataModel = DataModel.of(
                            CommentDto.of(comment),
                            linkTo(CommentController.class).slash(comment.getId())
                    );
                    return dataModel;
                });
    }

    public void getPagingByAuthorValidate(int page, int size, String sort, Member author) {

        Errors errors = AppConfig.getMockErrors("comment");

        if (this.commentRepository.countByAuthor(author) == 0) {

            errors.reject("not exist", new Object[]{author.getUsername()}, "comment that has author does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_03_01,
                            errors
                    )
            );
        }

        if (size != 20 && size != 50 && size != 100) {

            errors.reject("not allowed", new Object[]{size}, "size does not allowed");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_03_02,
                            errors
                    )
            );
        }

        if (Math.ceil((double) this.commentRepository.countByAuthor(author) / size) < page) {

            errors.reject("not exist", new Object[]{page}, "page does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_03_03,
                            errors
                    )
            );
        }

        if (!sort.equals("new") && !sort.equals("old") && !sort.equals("like")) {

            errors.reject("not allowed", new Object[]{sort}, "sort does not allowed");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_03_04,
                            errors
                    )
            );
        }
    }

    @Transactional
    public Comment update(CommentRequest.UpdateComment request, Errors errors, Long id, User user) {

        this.updateValidate(errors, id, user);

        Comment comment = this.getCommentById(id);

        comment = comment.toBuilder()
                .modifyDate(LocalDateTime.now())
                .content(request.getContent())
                .build();

        this.commentRepository.save(comment);

        return comment;
    }

    public void updateValidate(Errors errors, Long id, User user) {

        if (!this.commentRepository.existsById(id)) {

            errors.reject("not exist", new Object[]{id}, "comment that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_04_01,
                            errors
                    )
            );
        }

        if (errors.hasErrors()) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_04_02,
                            errors
                    )
            );
        }

        if (!this.getCommentById(id).getAuthor().getUsername().equals(user.getUsername())) {

            errors.reject("not authorized", new Object[]{user.getUsername()}, "member that has username is not authorized to modify");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_04_03,
                            errors
                    )
            );
        }
    }

    @Transactional
    public void delete(Long id, User user) {

        this.deleteValidate(id, user);

        this.commentRepository.deleteById(id);
    }

    public void deleteValidate(Long id, User user) {

        Errors errors = AppConfig.getMockErrors("comment");

        if (!this.commentRepository.existsById(id)) {

            errors.reject("not exist", new Object[]{id}, "comment that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_05_01,
                            errors
                    )
            );
        }

        if (!this.getCommentById(id).getAuthor().getUsername().equals(user.getUsername())) {

            errors.reject("not authorized", new Object[]{user.getUsername()}, "member that has username is not authorized to delete");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_05_02,
                            errors
                    )
            );
        }
    }

    @Transactional
    public Comment toggleLike(Long id, Member member) {

        this.toggleLikeValidate(id, member);

        Comment comment = this.getCommentById(id);

        if (comment.getLikedMember().contains(member)) {

            comment = comment.toBuilder()
                    .liked(comment.getLiked() - 1)
                    .build();
            comment.getLikedMember().remove(member);

        } else {

            comment = comment.toBuilder()
                    .liked(comment.getLiked() + 1)
                    .build();
            comment.getLikedMember().add(member);
        }

        this.commentRepository.save(comment);

        return comment;
    }

    public void toggleLikeValidate(Long id, Member member) {

        Errors errors = AppConfig.getMockErrors("comment");

        if (!this.commentRepository.existsById(id)) {

            errors.reject("not exist", new Object[]{id}, "comment that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_06_01,
                            errors
                    )
            );
        }

        if (this.getCommentById(id).getAuthor().getUsername().equals(member.getUsername())) {

            errors.reject("not authorized", new Object[]{member.getUsername()}, "author cannot like own comment");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_06_02,
                            errors
                    )
            );
        }
    }
}
