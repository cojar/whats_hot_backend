package com.cojar.whats_hot_backend.domain.spot_module.spot_category.repository;

import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot_category.entity.SpotCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotCategoryRepository extends JpaRepository<SpotCategory, Long> {

    List<SpotCategory> findAllBySpot(Spot spot);
}
