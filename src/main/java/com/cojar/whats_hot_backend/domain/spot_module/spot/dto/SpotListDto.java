package com.cojar.whats_hot_backend.domain.spot_module.spot.dto;

import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SpotListDto {

    private final Long id;

    private final String category;

    private final String name;

    private final String address;

    private final String contact;

    private final String averageScore;

    private final List<String> hashtags;

    private final List<String> imageUri;

    private final Integer reviews;

    public SpotListDto (Spot spot) {
        this.id = spot.getId();
        this.category = spot.getCategories().get(0).getCategory().toLine();
        this.name = spot.getName();
        this.address = spot.getAddress();
        this.contact = spot.getContact();
        this.averageScore = "%.2f".formatted(spot.getAverageScore());
        this.hashtags = spot.getHashtags().stream()
                .map(h -> h.getHashtag().getName())
                .collect(Collectors.toList());
        this.imageUri = spot.getImages().stream()
                .map(image -> image.getImage().toUri(AppConfig.getBaseFileURL()))
                .collect(Collectors.toList());
        this.reviews = spot.getReviewCount();
    }

    public static SpotListDto of(Spot spot) {
        return new SpotListDto(spot);
    }
}
