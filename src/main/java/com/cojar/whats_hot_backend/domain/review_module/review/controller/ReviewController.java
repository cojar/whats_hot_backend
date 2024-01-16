package com.cojar.whats_hot_backend.domain.review_module.review.controller;

import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.api_response.ReviewApiResponse;
import com.cojar.whats_hot_backend.domain.review_module.review.dto.ReviewCreateDto;
import com.cojar.whats_hot_backend.domain.review_module.review.dto.ReviewGetDto;
import com.cojar.whats_hot_backend.domain.review_module.review.dto.ReviewLikeDto;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.request.ReviewRequest;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.service.ReviewHashtagService;
import com.cojar.whats_hot_backend.domain.review_module.review_image.service.ReviewImageService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.PagedDataModel;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@Tag(name = "Review", description = "리뷰 서비스 API")
@RequestMapping(value = "/api/reviews", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final SpotService spotService;
    private final MemberService memberService;
    private final ReviewHashtagService reviewHashtagService;
    private final FileService fileService;
    private final ReviewImageService reviewImageService;

    @ReviewApiResponse.Create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createReview(@Valid @RequestPart(value = "request") ReviewRequest.CreateReview request, Errors errors,
                                       @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                       @AuthenticationPrincipal User user) {

        Review review = this.reviewService.create(request, images, errors, user);

        ResData resData = ResData.of(
                ResCode.S_03_01,
                ReviewCreateDto.of(review),
                linkTo(ReviewController.class).slash(review.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/createReview").withRel("profile"));
        return ResponseEntity.created(resData.getSelfUri())
                .body(resData);
    }

    @ReviewApiResponse.List
    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity getReviews(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "20") int size,
                                     @RequestParam(value = "sort", defaultValue = "like") String sort,
                                     @RequestParam(value = "spotId", defaultValue = "-1") Long spotId,
                                     @RequestParam(value = "image", defaultValue = "false") boolean image,
                                     HttpServletRequest request) {

        Page<DataModel> reviewList = this.reviewService.getReviewPages(page, size, sort, spotId, image);

        ResData resData = ResData.of(
                ResCode.S_03_02,
                PagedDataModel.of(reviewList),
                linkTo(this.getClass()).slash("?%s".formatted(request.getQueryString()))
        );

        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/getReviews").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @ReviewApiResponse.Detail
    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity getReview(@PathVariable(value = "id") Long id) {

        Review review = this.reviewService.getReviewById(id);

        ResData resData = ResData.of(
                ResCode.S_03_03,
                ReviewGetDto.of(review),
                linkTo(this.getClass()).slash(review.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/getReview").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }

    @ReviewApiResponse.Update
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateReview(@Valid @RequestPart(value = "request") ReviewRequest.UpdateReview request, Errors errors,
                                       @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                       @PathVariable(value = "id") Long id,
                                       @AuthenticationPrincipal User user) {

        Review review = this.reviewService.update(request, images, errors, id, user);

        ResData resData = ResData.of(
                ResCode.S_03_04,
                ReviewCreateDto.of(review),
                linkTo(this.getClass()).slash(review.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/updateReview").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }

    @ReviewApiResponse.Delete
    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity deleteReview(@PathVariable(value = "id") Long id,
                                       @AuthenticationPrincipal User user) {

        this.reviewService.delete(id, user);

        ResData resData = ResData.of(
                ResCode.S_03_05,
                linkTo(this.getClass())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/deleteReview").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }

    @ReviewApiResponse.Like
    @PatchMapping(value = "/{id}/like", consumes = MediaType.ALL_VALUE)
    public ResponseEntity likeReview(@PathVariable(value = "id") Long id,
                                     @AuthenticationPrincipal User user) {

        Member member = this.memberService.getUserByUsername(user.getUsername());
        Review review = this.reviewService.toggleLike(id, member);

        ResData resData = ResData.of(
                ResCode.S_03_06,
                ReviewLikeDto.of(review, member),
                linkTo(this.getClass()).slash(review.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/likeReview").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }

    @ReviewApiResponse.MyList
    @GetMapping(value = "/me", consumes = MediaType.ALL_VALUE)
    public ResponseEntity getMyReviews(@RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "size", defaultValue = "20") int size,
                                       @RequestParam(value = "sort", defaultValue = "new") String sort,
                                       @AuthenticationPrincipal User user,
                                       HttpServletRequest request) {

        Member member = this.memberService.getUserByUsername(user.getUsername());
        Page<DataModel> reviewList = this.reviewService.getMyReviewPages(page, size, sort, member);

        ResData resData = ResData.of(
                ResCode.S_03_07,
                PagedDataModel.of(reviewList),
                linkTo(this.getClass()).slash(request.getQueryString() != null ? "/me?%s".formatted(request.getQueryString()) : "/me")
        );

        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/getMyReviews").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }
}
