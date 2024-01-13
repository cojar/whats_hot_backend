package com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.repository;

import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.entity.SpotHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotHashtagRepository extends JpaRepository<SpotHashtag, Long> {
    List<SpotHashtag> findAllBySpot(Spot spot);
}
