package com.cojar.whats_hot_backend.domain.base_module.hashtag.repository;

import com.cojar.whats_hot_backend.domain.base_module.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
