package com.cojar.whats_hot_backend.domain.spot_module.spot.dto;

import com.cojar.whats_hot_backend.domain.review_module.review.dto.ReviewDto;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.dto.MenuItemDto;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class SpotDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String category;

    private final String name;

    private final String address;

    private final String contact;

    private final Double averageScore;

    private final List<String> hashtags;

    private final List<MenuItemDto> menuItems;

    private final List<String> imageUri;

    private final List<ReviewDto> reviews;

    public SpotDto(Spot spot) {
        this.id = spot.getId();
        this.createDate = spot.getCreateDate();
        this.modifyDate = spot.getModifyDate();
        this.category = spot.getCategories().get(0).getCategory().toLine();
        this.name = spot.getName();
        this.address = spot.getAddress();
        this.contact = spot.getContact();
        this.averageScore = spot.getAverageScore();
        this.hashtags = spot.getHashtags().stream()
                .map(h -> h.getHashtag().getName())
                .collect(Collectors.toList());
        this.menuItems = spot.getMenuItems().stream()
                .map(MenuItemDto::of)
                .collect(Collectors.toList());
        this.imageUri = spot.getImages().stream()
                .map(image -> image.getImage().toUri(AppConfig.getBaseFileURL()))
                .collect(Collectors.toList());
        this.reviews = spot.getReviews().stream()
                .map(ReviewDto::of)
                .collect(Collectors.toList());
    }

    public List<String> getHashtags() {
        if (this.hashtags.isEmpty()) return null;
        else return this.hashtags;
    }

    public List<MenuItemDto> getMenuItems() {
        if (this.menuItems.isEmpty()) return null;
        else return this.menuItems;
    }

    public List<String> getImageUri() {
        if (this.imageUri.isEmpty()) return null;
        else return this.imageUri;
    }

    public List<ReviewDto> getReviews() {
        if (this.reviews.isEmpty()) return null;
        else return this.reviews;
    }

    public static SpotDto of(Spot spot) {
        return new SpotDto(spot);
    }
}
