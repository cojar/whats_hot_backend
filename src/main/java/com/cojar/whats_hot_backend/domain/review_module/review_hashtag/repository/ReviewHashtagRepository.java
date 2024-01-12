package com.cojar.whats_hot_backend.domain.review_module.review_hashtag.repository;

import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.entity.ReviewHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewHashtagRepository extends JpaRepository<ReviewHashtag, Long> {
    List<ReviewHashtag> findAllByReview(Review review);
}
