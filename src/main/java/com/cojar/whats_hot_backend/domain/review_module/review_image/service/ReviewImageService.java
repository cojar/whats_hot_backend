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

    public List<ReviewImage> createAll(List<_File> files, Review review) {

        List<ReviewImage> reviewImages = files.stream()
                .map(file ->
                        ReviewImage.builder()
                                .image(file)
                                .review(review)
                                .build()
                )
                .collect(Collectors.toList());
        return reviewImages;
    }

    @Transactional
    public void saveAll(List<ReviewImage> reviewImages) {
        if (reviewImages != null) this.reviewImageRepository.saveAll(reviewImages);
    }
}
