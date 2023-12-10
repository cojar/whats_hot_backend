package com.cojar.whats_hot_backend.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryDto {

    @NotBlank
    private String first;

    @NotBlank
    private String second;

    @NotBlank
    private String third;
}
