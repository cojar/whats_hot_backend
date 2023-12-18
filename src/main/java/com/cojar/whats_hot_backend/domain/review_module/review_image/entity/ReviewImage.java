package com.cojar.whats_hot_backend.domain.review_module.review_image.entity;

import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class ReviewImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private _File image;
}
