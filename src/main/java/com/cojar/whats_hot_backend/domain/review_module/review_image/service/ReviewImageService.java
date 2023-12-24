package com.cojar.whats_hot_backend.domain.review_module.review_image.service;

import com.cojar.whats_hot_backend.domain.review_module.review_image.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;


}
