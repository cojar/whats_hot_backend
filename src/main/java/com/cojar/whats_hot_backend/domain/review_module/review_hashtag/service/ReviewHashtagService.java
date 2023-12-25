package com.cojar.whats_hot_backend.domain.review_module.review_hashtag.service;

import com.cojar.whats_hot_backend.domain.base_module.hashtag.entity.Hashtag;
import com.cojar.whats_hot_backend.domain.base_module.hashtag.repository.HashtagRepository;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.entity.ReviewHashtag;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.repository.ReviewHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewHashtagService {

    private final ReviewHashtagRepository reviewHashtagRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public List<ReviewHashtag> createAll(List<String> hashtags, Review review) {

        List<ReviewHashtag> reviewHashtags = hashtags.stream()
                .map(hashtag ->
                        ReviewHashtag.builder()
                                .hashtag(this.hashtagRepository.findByName(hashtag)
                                        .orElse(this.hashtagRepository.save(Hashtag.builder().name(hashtag).build())))
                                .review(review)
                                .build()
                )
                .collect(Collectors.toList());
        return reviewHashtags;
    }

    @Transactional
    public void saveAll(List<ReviewHashtag> reviewHashtags) {
        if (reviewHashtags != null) this.reviewHashtagRepository.saveAll(reviewHashtags);
    }
}
