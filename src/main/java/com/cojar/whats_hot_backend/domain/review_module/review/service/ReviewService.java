package com.cojar.whats_hot_backend.domain.review_module.review.service;

import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.review_module.review.dto.ReviewDto;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.ReviewStatus;
import com.cojar.whats_hot_backend.domain.review_module.review.repository.ReviewRepository;
import com.cojar.whats_hot_backend.domain.review_module.review.request.ReviewRequest;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.entity.ReviewHashtag;
import com.cojar.whats_hot_backend.domain.review_module.review_image.entity.ReviewImage;
import com.cojar.whats_hot_backend.domain.spot_module.spot.controller.SpotController;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.repository.SpotRepository;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SpotRepository spotRepository;

    public Review getReviewById(Long id) {
        return this.reviewRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public Review create(Member author, Spot spot, LocalDateTime visitDate, String title, String content, Double score, ReviewStatus status) {

        Review review = Review.builder()
                .author(author)
                .spot(spot)
                .visitDate(visitDate)
                .title(title)
                .content(content)
                .score(score)
                .status(status)
                .validated(true)
                .build();

        this.reviewRepository.save(review);

        return review;
    }

    public ResData createValidate(ReviewRequest.CreateReview request, Errors errors) {

        if (!this.spotRepository.existsById(request.getSpotId())) {

            errors.rejectValue("spotId", "not exist", "spot that has id does not exist");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-03-01-02",
                    "해당 아이디를 가진 장소가 존재하지 않습니다",
                    errors
            );
        }

        return null;
    }

    public Review create(ReviewRequest.CreateReview request, Spot spot, Member author) {

        Review review = Review.builder()
                .visitDate(LocalDateTime.of(request.getYear(), request.getMonth(), request.getDay(), 0, 0, 0))
                .title(request.getTitle())
                .content(request.getContent())
                .score(request.getScore())
                .spot(spot)
                .author(author)
                .status(request.isLock() ? ReviewStatus.PRIVATE : ReviewStatus.PUBLIC)
                .build();

        return this.reviewRepository.save(review);
    }

    @Transactional
    public void save(Review review) {
        this.reviewRepository.save(review);
    }

    public Page<DataModel> getReviewList(int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return this.reviewRepository.findAll(pageable)
                .map(review -> {
                    DataModel dataModel = DataModel.of(
                            ReviewDto.of(review),
                            linkTo(SpotController.class).slash(review.getId())
                    );
                    return dataModel;
                });
    }

    public Review updateHashtags(Review review, List<ReviewHashtag> reviewHashtags) {
        return review.toBuilder()
                .hashtags(reviewHashtags)
                .build();
    }

    public Review updateImages(Review review, List<ReviewImage> reviewImages) {
        return review.toBuilder()
                .images(reviewImages)
                .build();
    }
}
