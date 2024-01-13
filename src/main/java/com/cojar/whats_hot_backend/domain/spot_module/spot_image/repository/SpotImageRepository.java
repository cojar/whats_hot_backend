package com.cojar.whats_hot_backend.domain.spot_module.spot_image.repository;

import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.entity.SpotImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotImageRepository extends JpaRepository<SpotImage, Long> {
    List<SpotImage> findAllBySpot(Spot spot);
}
