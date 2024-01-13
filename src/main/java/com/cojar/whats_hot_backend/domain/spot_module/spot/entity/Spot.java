package com.cojar.whats_hot_backend.domain.spot_module.spot.entity;

import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.entity.MenuItem;
import com.cojar.whats_hot_backend.domain.spot_module.spot_category.entity.SpotCategory;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.entity.SpotHashtag;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.entity.SpotImage;
import com.cojar.whats_hot_backend.global.jpa.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Entity
public class Spot extends BaseEntity {

    @OneToMany(mappedBy = "spot", cascade = CascadeType.REMOVE)
    List<SpotCategory> categories;

    private String name;

    private String address;

    private Double latitude;

    private Double longitude;

    private String contact;

    @Builder.Default
    private Double averageScore = 0.0;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.REMOVE)
    private List<SpotHashtag> hashtags;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.REMOVE)
    private List<MenuItem> menuItems;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.REMOVE)
    private List<SpotImage> images;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.REMOVE)
    private List<Review> reviews;
}
