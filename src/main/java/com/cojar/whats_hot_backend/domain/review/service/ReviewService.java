package com.cojar.whats_hot_backend.domain.review.service;

import com.cojar.whats_hot_backend.domain.member.entity.Member;
import com.cojar.whats_hot_backend.domain.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review.entity.ReviewStatus;
import com.cojar.whats_hot_backend.domain.review.repository.ReviewRepository;
import com.cojar.whats_hot_backend.domain.spot.entity.Spot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
