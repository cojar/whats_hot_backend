package com.cojar.whats_hot_backend.domain.spot_module.spot.repository;

import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {

    Page<Spot> findAll(Pageable pageable);

    Page<Spot> findAll(Specification<Spot> spec, Pageable pageable);

    boolean existsByNameAndAddress(String name, String address);

    Optional<Spot> findByNameAndAddress(String name, String address);
}
