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

    public long count() {
        return this.reviewHashtagRepository.count();
    }

    @Transactional
    public List<ReviewHashtag> createAll(List<String> hashtags, Review review) {

        if (hashtags == null) return null;

        List<ReviewHashtag> reviewHashtags = hashtags.stream()
                .map(hashtag -> {
                            Hashtag _hashtag = this.hashtagRepository.findByName(hashtag).orElse(null);
                            if (_hashtag == null) {
                                _hashtag = Hashtag.builder().name(hashtag).build();
                                this.hashtagRepository.save(_hashtag);
                            }
                            return ReviewHashtag.builder()
                                    .hashtag(_hashtag)
                                    .review(review)
                                    .build();
                        }
                )
                .collect(Collectors.toList());

        this.reviewHashtagRepository.saveAll(reviewHashtags);

        return reviewHashtags;
    }
}
