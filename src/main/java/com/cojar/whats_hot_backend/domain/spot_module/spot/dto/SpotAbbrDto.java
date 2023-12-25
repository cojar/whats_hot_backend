package com.cojar.whats_hot_backend.domain.spot_module.spot.dto;

import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class SpotAbbrDto {

    private final Long id;

    private final Double averageScore;

    private final Integer reviews;

    public SpotAbbrDto(Spot spot) {
        this.id = spot.getId();
        this.averageScore = spot.getAverageScore();
        this.reviews = spot.getReviews().size();
    }

    public static SpotAbbrDto of(Spot spot) {
        return new SpotAbbrDto(spot);
    }
}
