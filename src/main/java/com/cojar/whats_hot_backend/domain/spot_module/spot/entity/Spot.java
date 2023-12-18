package com.cojar.whats_hot_backend.domain.spot_module.spot.entity;

import com.cojar.whats_hot_backend.domain.base_module.hashtag.entity.Hashtag;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.entity.MenuItem;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.entity.SpotImage;
import com.cojar.whats_hot_backend.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Spot extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    private String address;

    private String contact;

    @Builder.Default
    private Double averageScore = 0.0;

    @ManyToMany
    private List<Hashtag> hashtags;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.REMOVE)
    private List<MenuItem> menuItems;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.REMOVE)
    private List<SpotImage> images;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.REMOVE)
    private List<Review> reviews;
}
