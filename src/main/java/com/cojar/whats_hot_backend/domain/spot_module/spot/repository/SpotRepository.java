package com.cojar.whats_hot_backend.domain.spot_module.spot.repository;

import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    boolean existsByNameAndAddress(String name, String address);

    Optional<Spot> findByNameAndAddress(String name, String address);
}
