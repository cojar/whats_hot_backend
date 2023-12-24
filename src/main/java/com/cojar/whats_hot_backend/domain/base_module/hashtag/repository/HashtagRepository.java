package com.cojar.whats_hot_backend.domain.base_module.hashtag.repository;

import com.cojar.whats_hot_backend.domain.base_module.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String name);
}
