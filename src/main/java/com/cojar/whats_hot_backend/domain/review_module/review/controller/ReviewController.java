package com.cojar.whats_hot_backend.domain.review_module.review.controller;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.api_response.ReviewApiResponse;
import com.cojar.whats_hot_backend.domain.review_module.review.dto.ReviewDto;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.request.ReviewRequest;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.entity.ReviewHashtag;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.service.ReviewHashtagService;
import com.cojar.whats_hot_backend.domain.review_module.review_image.entity.ReviewImage;
import com.cojar.whats_hot_backend.domain.review_module.review_image.service.ReviewImageService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.PagedDataModel;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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

        ResData resData = this.reviewService.createValidate(request, errors);
        if (resData != null) return ResponseEntity.badRequest().body(resData);

        Spot spot = this.spotService.getSpotById(request.getSpotId());
        Member author = this.memberService.getUserByUsername(user.getUsername());

        Review review = this.reviewService.create(request, spot, author);
        spot = this.spotService.updateReview(spot, review);

        // hashtags 생성
        List<ReviewHashtag> reviewHashtags = null;
        if (request.getHashtags() != null) {
            resData = this.fileService.validateAll(images);
            if (resData != null) return ResponseEntity.badRequest().body(resData);
            reviewHashtags = this.reviewHashtagService.createAll(request.getHashtags(), review);
            review = this.reviewService.updateHashtags(review, reviewHashtags);
        }

        // images 생성
        List<_File> files = null;
        List<ReviewImage> reviewImages = null;
        if (images != null) {

            files = this.fileService.createAll(images, FileDomain.REVIEW);
            reviewImages = this.reviewImageService.createAll(files, review);
            review = this.reviewService.updateImages(review, reviewImages);
        }

        this.reviewHashtagService.saveAll(reviewHashtags);
        this.fileService.saveAll(files);
        this.reviewImageService.saveAll(reviewImages);
        this.reviewService.save(review);
        this.spotService.save(spot);

        resData = ResData.of(
                HttpStatus.CREATED,
                "S-03-01",
                "리뷰 등록이 완료되었습니다",
                ReviewDto.of(review),
                linkTo(ReviewController.class).slash(review.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/createReview").withRel("profile"));
        return ResponseEntity.created(resData.getSelfUri())
                .body(resData);
    }

    @ReviewApiResponse.List
    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity getReviewList(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "2") int size) {

        Page<DataModel> reviewList = this.reviewService.getReviewList(page, size);

        ResData resData = ResData.of(
                HttpStatus.OK,
                "S-03-02",
                "요청하신 리뷰 목록을 반환합니다",
                PagedDataModel.of(reviewList),
                linkTo(this.getClass()).slash("?page=%s&size=%s".formatted(page, size))
        );

        // TODO: paged links with query; custom method
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/getReviewList").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @ReviewApiResponse.Detail
    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity getReview(@PathVariable(value = "id") Long id) {

        Review review = this.reviewService.getReviewById(id);

        ResData resData = ResData.of(
                HttpStatus.OK,
                "S-03-03",
                "요청하신 리뷰 정보를 반환합니다",
                ReviewDto.of(review),
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

        Review review = this.reviewService.getReviewById(id);

        ResData resData = ResData.of(
                HttpStatus.OK,
                "S-03-04",
                "리뷰 수정이 완료되었습니다",
                ReviewDto.of(review),
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

        ResData resData = ResData.of(
                HttpStatus.OK,
                "S-03-05",
                "리뷰 삭제가 완료되었습니다",
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

        Review review = this.reviewService.getReviewById(id);

        ResData resData = ResData.of(
                HttpStatus.OK,
                "S-03-06",
                "리뷰 좋아요 상태가 변경되었습니다",
                ReviewDto.of(review),
                linkTo(this.getClass()).slash(review.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/likeReview").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }
}
