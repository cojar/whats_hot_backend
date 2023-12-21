package com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.service;

import com.cojar.whats_hot_backend.domain.base_module.hashtag.entity.Hashtag;
import com.cojar.whats_hot_backend.domain.base_module.hashtag.repository.HashtagRepository;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.repository.SpotRepository;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.entity.SpotHashtag;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.repository.SpotHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SpotHashtagService {

    private final SpotHashtagRepository spotHashtagRepository;
    private final SpotRepository spotRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public List<SpotHashtag> createAll(List<String> hashtags, Spot spot) {

        List<SpotHashtag> spotHashtags = hashtags.stream()
                .map(hashtag ->
                        SpotHashtag.builder()
                                .hashtag(this.hashtagRepository.findByName(hashtag)
                                        .orElse(this.hashtagRepository.save(Hashtag.builder().name(hashtag).build())))
                                .spot(spot)
                                .build()
                )
                .collect(Collectors.toList());

        this.spotHashtagRepository.saveAll(spotHashtags);
        spot.updateHashtags(spotHashtags);
        return spotHashtags;
    }
}
