package com.cojar.whats_hot_backend.domain.spot.dto;

import com.cojar.whats_hot_backend.domain.hashtag.dto.HashtagDto;
import com.cojar.whats_hot_backend.domain.spot.entity.Spot;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SpotListDto {

    private final Long id;

    private final String category;

    private final String address;

    private final String contact;

    private final Double averageScore;

    private final List<HashtagDto> hashtags;

    private final Integer reviews;

    public SpotListDto (Spot spot) {
        this.id = spot.getId();
        this.category = spot.getCategory().toLine();
        this.address = spot.getAddress();
        this.contact = spot.getContact();
        this.averageScore = spot.getAverageScore();
        this.hashtags = spot.getHashtags().stream()
                .map(HashtagDto::of)
                .collect(Collectors.toList());
        this.reviews = spot.getReviews().size();
    }

    public static SpotListDto of(Spot spot) {
        return new SpotListDto(spot);
    }
}
