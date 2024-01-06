package com.cojar.whats_hot_backend.domain.comment_module.comment.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CommentRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateComment {

        @Schema(example = "댓글내용1")
        @NotBlank
        private String content;

        @Schema(example = "1")
        @NotNull
        private Long reviewId;

        @Schema(example = "1")
        private Long tagId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateComment {

        @Schema(example = "댓글내용1")
        @NotBlank
        private String content;
    }
}
