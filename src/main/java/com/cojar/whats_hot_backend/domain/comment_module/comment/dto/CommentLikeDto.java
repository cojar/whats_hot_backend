package com.cojar.whats_hot_backend.domain.comment_module.comment.dto;

import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CommentLikeDto {

    private final Long liked;

    private final boolean like;

    public CommentLikeDto (Comment comment, Member member) {
        this.liked = comment.getLiked();
        this.like = comment.getLikedMember().contains(member);
    }

    public static CommentLikeDto of(Comment comment, Member member) {
        return new CommentLikeDto(comment, member);
    }
}
