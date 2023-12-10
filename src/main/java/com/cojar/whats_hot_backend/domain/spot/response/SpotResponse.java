package com.cojar.whats_hot_backend.domain.spot.response;

import com.cojar.whats_hot_backend.domain.spot.dto.SpotDto;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class SpotResponse {

    @Getter
    @AllArgsConstructor
    public static class Create {

        @JsonUnwrapped
        private final SpotDto spotDto;
    }
}
