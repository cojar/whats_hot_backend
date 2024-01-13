package com.cojar.whats_hot_backend.domain.spot_module.spot.request;

import com.cojar.whats_hot_backend.domain.spot_module.menu_item.dto.MenuItemDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class SpotRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateSpot {

        @NotNull
        private Long categoryId;

        @NotBlank
        private String name;

        @NotBlank
        private String address;

        @NotBlank
        private String contact;

        private List<@NotBlank String> hashtags;

        @Valid
        private List<MenuItemDto> menuItems;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateSpot {

        private Long categoryId;

        private String name;

        private String address;

        private String contact;

        private List<@NotBlank String> hashtags;

        @Valid
        private List<MenuItemDto> menuItems;
    }

    @Getter
    public static class Lists {

        private int page;

        private int size;
    }
}
