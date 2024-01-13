package com.cojar.whats_hot_backend.domain.review_module.review_image.repository;

import com.cojar.whats_hot_backend.domain.review_module.review_image.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
