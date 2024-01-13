package com.cojar.whats_hot_backend.domain.spot_module.category.dto;

import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.spot.dto.SpotDto;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {

    private final Long id;

    private final LocalDateTime createDate;

    private final LocalDateTime modifyDate;

    private final String name;

    private final Integer depth;

    private final Long parentId;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.createDate = category.getCreateDate();
        this.modifyDate = category.getModifyDate();
        this.name = category.getName();
        this.depth = category.getDepth();
        this.parentId = category.getParent() == null ? null : category.getParent().getId();
    }
    public static CategoryDto of(Category category) {
        return new CategoryDto(category);
    }
}
