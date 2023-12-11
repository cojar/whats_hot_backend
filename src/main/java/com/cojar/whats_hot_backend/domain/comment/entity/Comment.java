package com.cojar.whats_hot_backend.domain.comment.entity;

import com.cojar.whats_hot_backend.domain.member.entity.Member;
import com.cojar.whats_hot_backend.domain.review.entity.Review;
import com.cojar.whats_hot_backend.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Comment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Builder.Default
    private Long liked = 0L;

    @ManyToMany
    private Set<Member> likedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment tag;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> children;
}
