package com.cojar.whats_hot_backend.domain.comment_module.comment.dto;

import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
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

    private final boolean like;

    private final String author;

    private final Long reviewId;

    private final Long tagId;

    public CommentDto (Comment comment, Member member) {
        this.id = comment.getId();
        this.createDate = comment.getCreateDate();
        this.modifyDate = comment.getModifyDate();
        this.content = comment.getContent();
        this.liked = comment.getLiked();
        this.like = member != null && comment.getLikedMember().contains(member);
        this.author = comment.getAuthor().getUsername();
        this.reviewId = comment.getReview().getId();
        this.tagId = comment.getTag() != null ? comment.getTag().getId() : null;
    }
    public static CommentDto of(Comment comment, Member member) {
        return new CommentDto(comment, member);
    }

}
