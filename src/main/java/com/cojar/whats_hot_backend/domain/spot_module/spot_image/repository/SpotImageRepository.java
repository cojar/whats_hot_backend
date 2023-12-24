package com.cojar.whats_hot_backend.domain.spot_module.spot_image.repository;

import com.cojar.whats_hot_backend.domain.spot_module.spot_image.entity.SpotImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotImageRepository extends JpaRepository<SpotImage, Long> {
}
