package com.cojar.whats_hot_backend.domain.spot_module.category.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
public class CategoryRequest {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateCategory {

        @NotBlank
        private String name;

        private Long parentId;

        private Boolean allowRoot;
    }
}
