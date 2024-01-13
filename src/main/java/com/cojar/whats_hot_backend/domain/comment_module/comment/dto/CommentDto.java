package com.cojar.whats_hot_backend.domain.comment_module.comment.dto;

import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CommentDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String content;

    private final Long liked;

    private final String author;

    private final Long reviewId;

    private final Long tagId;

    public CommentDto (Comment comment) {
        this.id = comment.getId();
        this.createDate = comment.getCreateDate();
        this.modifyDate = comment.getModifyDate();
        this.content = comment.getContent();
        this.liked = comment.getLiked();
        this.author = comment.getAuthor().getUsername();
        this.reviewId = comment.getReview().getId();
        this.tagId = comment.getTag() != null ? comment.getTag().getId() : null;
    }
    public static CommentDto of(Comment comment) {
        return new CommentDto(comment);
    }

}
