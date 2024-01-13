package com.cojar.whats_hot_backend.domain.spot_module.menu_item.dto;

import com.cojar.whats_hot_backend.domain.spot_module.menu_item.entity.MenuItem;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuItemDto {

    @NotBlank
    private final String name;

    @NotBlank
    private final String price;

    @JsonCreator
    public MenuItemDto(@JsonProperty("name") String name,
                       @JsonProperty("price") String price) {
        this.name = name;
        this.price = price;
    }

    public MenuItemDto(MenuItem menuItem) {
        this.name = menuItem.getName();
        this.price = menuItem.getPrice();
    }

    public static MenuItemDto of(MenuItem menuItem) {
        return new MenuItemDto(menuItem);
    }

    public static MenuItemDto of(String name, String price) {
        return new MenuItemDto(name, price);
    }
}
