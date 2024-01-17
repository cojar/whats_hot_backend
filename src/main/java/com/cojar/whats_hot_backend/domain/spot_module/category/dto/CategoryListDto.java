package com.cojar.whats_hot_backend.domain.spot_module.category.dto;

import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryListDto {

    private final Long id;

    private final String name;

    public CategoryListDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
    public static CategoryListDto of(Category category) {
        return new CategoryListDto(category);
    }
}
