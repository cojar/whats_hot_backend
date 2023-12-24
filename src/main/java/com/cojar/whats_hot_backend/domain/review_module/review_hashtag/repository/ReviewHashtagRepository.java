package com.cojar.whats_hot_backend.domain.review_module.review_hashtag.repository;

import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.entity.ReviewHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewHashtagRepository extends JpaRepository<ReviewHashtag, Long> {
}
