package com.cojar.whats_hot_backend.domain.review_module.review.request;

import com.cojar.whats_hot_backend.domain.review_module.review.entity.ReviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReviewRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateReview {

        @Schema(example = "1")
        @NotNull
        private Long spotId;

        @NotNull
        private Integer year;

        @NotNull
        private Integer month;

        @NotNull
        private Integer day;

        @Schema(example = "리뷰제목1")
        @NotBlank
        private String title;

        @Schema(example = "리뷰내용1")
        @NotBlank
        private String content;

        @Schema(example = "4.5")
        @NotNull
        private Double score;

        private List<@NotBlank String> hashtags;

        @Schema(example = "true")
        @Builder.Default
        private boolean lock = false;
    }

    @Getter
    public static class UpdateReview {

        @Schema(example = "1")
        private Long spotId;

        private LocalDateTime visitDate;

        @Schema(example = "리뷰제목1")
        private String title;

        @Schema(example = "리뷰내용1")
        private String content;

        @Schema(example = "4.5")
        private Double score;

        @Schema(example = "PUBLIC")
        private ReviewStatus status;
    }
}
