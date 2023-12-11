package com.cojar.whats_hot_backend.domain.review.request;

import com.cojar.whats_hot_backend.domain.review.entity.ReviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewRequest {

    @Getter
    public static class CreateReview {

        @Schema(example = "1")
        @NotNull
        private Long spotId;

        @NotNull
        private LocalDateTime visitDate;

        @Schema(example = "리뷰제목1")
        @NotBlank
        private String title;

        @Schema(example = "리뷰내용1")
        @NotBlank
        private String content;

        @Schema(example = "4.5")
        @NotNull
        private Double score;

        @Schema(example = "PUBLIC")
        @NotNull
        private ReviewStatus status;
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
