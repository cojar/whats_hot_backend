package com.cojar.whats_hot_backend.domain.spot_module.spot.dto;

import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class SpotStarDto {

    private final Long starred;

    private final boolean star;

    public SpotStarDto(Spot spot, Member member) {
        this.starred = spot.getStarred();
        this.star = spot.getStarredMember().contains(member);
    }

    public static SpotStarDto of(Spot spot, Member member) {
        return new SpotStarDto(spot, member);
    }
}
