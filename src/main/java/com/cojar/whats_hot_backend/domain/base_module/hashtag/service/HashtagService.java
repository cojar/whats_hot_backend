package com.cojar.whats_hot_backend.domain.base_module.hashtag.service;

import com.cojar.whats_hot_backend.domain.base_module.hashtag.entity.Hashtag;
import com.cojar.whats_hot_backend.domain.base_module.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public Hashtag create(String name) {

        Hashtag hashtag = Hashtag.builder()
                .name(name)
                .build();

        this.hashtagRepository.save(hashtag);

        return hashtag;
    }
}
