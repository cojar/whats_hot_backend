package com.cojar.whats_hot_backend.domain.review_module.review.dto;

import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ReviewLikeDto {

    private final Long liked;

    private final boolean like;

    public ReviewLikeDto(Review review, Member member) {
        this.liked = review.getLiked();
        this.like = review.getLikedMember().contains(member);
    }

    public static ReviewLikeDto of(Review review, Member member) {
        return new ReviewLikeDto(review, member);
    }
}
