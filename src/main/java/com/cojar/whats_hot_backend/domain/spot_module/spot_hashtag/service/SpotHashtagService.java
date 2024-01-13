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

    public long count() {
        return this.spotHashtagRepository.count();
    }

    @Transactional
    public List<SpotHashtag> createAll(List<String> hashtags, Spot spot) {

        if (hashtags == null) return null;

        List<SpotHashtag> spotHashtags = hashtags.stream()
                .map(hashtag -> {
                            Hashtag _hashtag = this.hashtagRepository.findByName(hashtag).orElse(null);
                            if (_hashtag == null) {
                                _hashtag = Hashtag.builder().name(hashtag).build();
                                this.hashtagRepository.save(_hashtag);
                            }
                            return SpotHashtag.builder()
                                    .hashtag(_hashtag)
                                    .spot(spot)
                                    .build();
                        }
                )
                .collect(Collectors.toList());

        this.spotHashtagRepository.saveAll(spotHashtags);

        return spotHashtags;
    }

    public List<SpotHashtag> getAllBySpot(Spot spot) {
        return this.spotHashtagRepository.findAllBySpot(spot);
    }

    @Transactional
    public List<SpotHashtag> updateAll(List<String> hashtags, Spot spot) {

        if (hashtags == null) return null;

        List<SpotHashtag> spotHashtags = hashtags.stream()
                .map(hashtag -> {
                            Hashtag _hashtag = this.hashtagRepository.findByName(hashtag).orElse(null);
                            if (_hashtag == null) {
                                _hashtag = Hashtag.builder().name(hashtag).build();
                                this.hashtagRepository.save(_hashtag);
                            }
                            return SpotHashtag.builder()
                                    .hashtag(_hashtag)
                                    .spot(spot)
                                    .build();
                        }
                )
                .collect(Collectors.toList());

        this.spotHashtagRepository.deleteAll(spot.getHashtags());
        this.spotHashtagRepository.saveAll(spotHashtags);

        return spotHashtags;
    }
}
