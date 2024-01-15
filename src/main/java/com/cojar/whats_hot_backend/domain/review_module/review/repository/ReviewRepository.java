package com.cojar.whats_hot_backend.domain.review_module.review.repository;

import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "select distinct r.* "
            + "from review r "
            + "left outer join review_image ri on r.id = ri.review_id "
            + "where (r.spot_id = :spot_id) "
            + "and ((:image = true and ri.id is not null) or (:image = false))"
            , nativeQuery = true)
    Page<Review> findAllBySpotAndImages(@Param("spot_id") Long spotId, @Param("image") boolean image, Pageable pageable);

    int countBySpot(Spot spot);
}
