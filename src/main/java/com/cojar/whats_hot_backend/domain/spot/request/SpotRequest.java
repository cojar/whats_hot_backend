package com.cojar.whats_hot_backend.domain.spot.request;

import com.cojar.whats_hot_backend.domain.category.dto.CategoryDto;
import com.cojar.whats_hot_backend.domain.hashtag.dto.HashtagDto;
import com.cojar.whats_hot_backend.domain.menu_item.dto.MenuItemDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class SpotRequest {

    @Getter
    public static class Create {

        @Valid
        private CategoryDto category;

        @NotBlank
        private String address;

        @NotBlank
        private String contact;

        @Valid
        @NotNull
        private List<HashtagDto> hashtags;

        @Valid
        private List<MenuItemDto> menuItems;
    }

    @Getter
    public static class Update {

        @Valid
        private CategoryDto category;

        private String address;

        private String contact;

        @Valid
        private List<HashtagDto> hashtags;

        @Valid
        private List<MenuItemDto> menuItems;

        private List<String> imageUri;
    }

    @Getter
    public static class Lists {

        private int page;

        private int size;
    }
}
