package com.cojar.whats_hot_backend.domain.review_module.review_hashtag.service;

import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.repository.ReviewHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewHashtagService {

    private final ReviewHashtagRepository reviewHashtagRepository;


}
