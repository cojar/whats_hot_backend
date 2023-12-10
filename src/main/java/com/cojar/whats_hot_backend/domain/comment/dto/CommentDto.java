package com.cojar.whats_hot_backend.domain.comment.dto;

import com.cojar.whats_hot_backend.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String author;

    private final String content;

    private final Long liked;

    private final Long tagId;

    public CommentDto (Comment comment) {
        this.id = comment.getId();
        this.createDate = comment.getCreateDate();
        this.modifyDate = comment.getModifyDate();
        this.author = comment.getAuthor().getUsername();
        this.content = comment.getContent();
        this.liked = comment.getLiked();
        this.tagId = comment.getTag().getId();
    }

    public static CommentDto of(Comment comment) {
        return new CommentDto(comment);
    }
}
