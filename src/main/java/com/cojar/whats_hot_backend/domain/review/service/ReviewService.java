package com.cojar.whats_hot_backend.domain.review.service;

import com.cojar.whats_hot_backend.domain.member.entity.Member;
import com.cojar.whats_hot_backend.domain.review.dto.ReviewDto;
import com.cojar.whats_hot_backend.domain.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review.entity.ReviewStatus;
import com.cojar.whats_hot_backend.domain.review.repository.ReviewRepository;
import com.cojar.whats_hot_backend.domain.spot.controller.SpotController;
import com.cojar.whats_hot_backend.domain.spot.entity.Spot;
import com.cojar.whats_hot_backend.global.response.DataModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review getReviewById(Long id) {
        return this.reviewRepository.findById(id)
                .orElse(null);
    }

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
}
