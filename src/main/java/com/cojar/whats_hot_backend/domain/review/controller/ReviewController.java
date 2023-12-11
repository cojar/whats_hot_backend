package com.cojar.whats_hot_backend.domain.review.controller;

import com.cojar.whats_hot_backend.domain.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review.api_response.ReviewApiResponse;
import com.cojar.whats_hot_backend.domain.review.dto.ReviewDto;
import com.cojar.whats_hot_backend.domain.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review.request.ReviewRequest;
import com.cojar.whats_hot_backend.domain.review.service.ReviewService;
import com.cojar.whats_hot_backend.domain.spot.controller.SpotController;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Tag(name = "Review", description = "리뷰 서비스 API")
@RequestMapping(value = "/api/reviews", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;

    @ReviewApiResponse.Create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createReview(@Valid @RequestPart(value = "request") ReviewRequest.CreateReview request, Errors errors,
                                       @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                       @AuthenticationPrincipal User user) {

        Member author = this.memberService.getUserByUsername(user.getUsername());

        Review review = this.reviewService.getReviewById(1L);

        ResData resData = ResData.of(
                HttpStatus.CREATED,
                "S-03-01",
                "리뷰 등록이 완료되었습니다",
                ReviewDto.of(review),
                linkTo(SpotController.class).slash(review.getSpot().getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/createReview").withRel("profile"));
        return ResponseEntity.created(resData.getSelfUri())
                .body(resData);
    }
}
