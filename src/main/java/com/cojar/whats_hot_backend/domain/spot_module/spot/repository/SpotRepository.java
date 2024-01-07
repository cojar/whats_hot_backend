package com.cojar.whats_hot_backend.domain.spot_module.spot.repository;

import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {

    Page<Spot> findAll(Pageable pageable);

    Page<Spot> findAll(Specification<Spot> spec, Pageable pageable);

    boolean existsByNameAndAddress(String name, String address);

    Optional<Spot> findByNameAndAddress(String name, String address);



    @Query("SELECT DISTINCT s FROM Spot s LEFT JOIN s.hashtags h LEFT JOIN s.category c WHERE " +
            "(c.depth = 1 OR c.depth = 2) AND " +
            "(s.name LIKE %:kw% OR h.name LIKE %:kw%)")
    List<Spot> searchSpotWithCategoryAndKeywordOrNoCategory(
            @Param("kw") String kw
    );
    }


