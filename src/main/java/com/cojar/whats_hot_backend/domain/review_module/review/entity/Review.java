package com.cojar.whats_hot_backend.domain.review_module.review.entity;

import com.cojar.whats_hot_backend.domain.comment_module.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.entity.ReviewHashtag;
import com.cojar.whats_hot_backend.domain.review_module.review_image.entity.ReviewImage;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Spot spot;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    private LocalDateTime visitDate;

    private String title;

    private String content;

    private Double score;

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<ReviewHashtag> hashtags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<ReviewImage> images = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private ReviewStatus status;

    private boolean validated;

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    private Long liked = 0L;

    @ManyToMany
    private Set<Member> likedMember;
}
