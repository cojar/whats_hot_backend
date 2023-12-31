package com.cojar.whats_hot_backend.domain.review_module.review.dto;

import com.cojar.whats_hot_backend.domain.comment_module.comment.dto.CommentDto;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ReviewDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String author;

    private final LocalDateTime visitDate;

    private final String title;

    private final String content;

    private final Double score;

    private final List<String> imageUri;

    private final String status;

    private final boolean validated;

    private final Long liked;

    private final List<CommentDto> comments;

    public ReviewDto (Review review) {
        this.id = review.getId();
        this.createDate = review.getCreateDate();
        this.modifyDate = review.getModifyDate();
        this.author = review.getAuthor().getUsername();
        this.visitDate = review.getVisitDate();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.score = review.getScore();
        this.imageUri = review.getImages().stream()
                .map(image -> image.toUri())
                .collect(Collectors.toList());
        this.status = review.getStatus().getType();
        this.validated = review.isValidated();
        this.liked = review.getLiked();
        this.comments = review.getComments().stream()
                .map(CommentDto::of)
                .collect(Collectors.toList());
    }

    public List<String> getImageUri() {
        if (this.imageUri.isEmpty()) return null;
        else return this.imageUri;
    }

    public List<CommentDto> getComments() {
        if (this.comments.isEmpty()) return null;
        else return this.comments;
    }

    public static ReviewDto of(Review review) {
        return new ReviewDto(review);
    }
}
