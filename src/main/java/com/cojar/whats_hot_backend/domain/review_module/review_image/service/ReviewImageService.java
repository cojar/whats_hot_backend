package com.cojar.whats_hot_backend.domain.review_module.review_image.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review_image.entity.ReviewImage;
import com.cojar.whats_hot_backend.domain.review_module.review_image.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;

    public long count() {
        return this.reviewImageRepository.count();
    }

    @Transactional
    public List<ReviewImage> createAll(List<_File> files, Review review) {

        if (files == null) return null;

        List<ReviewImage> reviewImages = files.stream()
                .map(file ->
                        ReviewImage.builder()
                                .image(file)
                                .review(review)
                                .build()
                )
                .collect(Collectors.toList());

        this.reviewImageRepository.saveAll(reviewImages);

        return reviewImages;
    }
}
