package com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.repository;

import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.entity.SpotHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotHashtagRepository extends JpaRepository<SpotHashtag, Long> {
}
