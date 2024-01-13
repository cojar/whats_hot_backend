package com.cojar.whats_hot_backend.domain.review_module.review.repository;

import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
