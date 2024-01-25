package com.cojar.whats_hot_backend.domain.spot_module.spot.repository;

import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {

    boolean existsByNameAndAddress(String name, String address);

    Optional<Spot> findByNameAndAddress(String name, String address);

    @Query(value = "select distinct s.* "
            + "from spot s "
            + "left outer join spot_category sc on s.id = sc.spot_id "
            + "left outer join category c on sc.category_id = c.id "
            + "left outer join spot_hashtag sh on s.id = sh.spot_id "
            + "left outer join hashtag h on sh.hashtag_id = h.id "
            + "left outer join review r on s.id = r.spot_id "
            + "left outer join review_hashtag rh on r.id = rh.review_id "
            + "left outer join hashtag h2 on rh.hashtag_id = h2.id "
            + "where (s.address like :region%) "
            + "and ((:category_id != -1 and c.id = :category_id) or (:category_id = -1)) "
            + "and ((:target = 'all' and (s.name like %:kw% or s.address like %:kw% or h.name like %:kw% or h2.name like %:kw%)) "
            + "or (:target = 'hashtag' and (h.name like %:kw% or h2.name like %:kw%)) "
            + "or (:target = 'name' and s.name like %:kw%))"
            , countQuery = "select count(distinct s.id) "
            + "from spot s "
            + "left outer join spot_category sc on s.id = sc.spot_id "
            + "left outer join category c on sc.category_id = c.id "
            + "left outer join spot_hashtag sh on s.id = sh.spot_id "
            + "left outer join hashtag h on sh.hashtag_id = h.id "
            + "left outer join review r on s.id = r.spot_id "
            + "left outer join review_hashtag rh on r.id = rh.review_id "
            + "left outer join hashtag h2 on rh.hashtag_id = h2.id "
            + "where (s.address like :region%) "
            + "and ((:category_id != -1 and c.id = :category_id) or (:category_id = -1)) "
            + "and ((:target = 'all' and (s.name like %:kw% or s.address like %:kw% or h.name like %:kw% or h2.name like %:kw%)) "
            + "or (:target = 'hashtag' and (h.name like %:kw% or h2.name like %:kw%)) "
            + "or (:target = 'name' and s.name like %:kw%))"
            , nativeQuery = true)
    Page<Spot> findAllByRegionAndCategoryIdAndKwAndTarget(@Param("region") String region,
                                                          @Param("category_id") Long categoryId,
                                                          @Param("kw") String kw,
                                                          @Param("target") String target,
                                                          Pageable pageable);

    @Query(value = "select count(distinct s.id) "
            + "from spot s "
            + "left outer join spot_category sc on s.id = sc.spot_id "
            + "left outer join category c on sc.category_id = c.id "
            + "left outer join spot_hashtag sh on s.id = sh.spot_id "
            + "left outer join hashtag h on sh.hashtag_id = h.id "
            + "left outer join review r on s.id = r.spot_id "
            + "left outer join review_hashtag rh on r.id = rh.review_id "
            + "left outer join hashtag h2 on rh.hashtag_id = h2.id "
            + "where (s.address like :region%) "
            + "and ((:category_id != -1 and c.id = :category_id) or (:category_id = -1)) "
            + "and ((:target = 'all' and (s.name like %:kw% or s.address like %:kw% or h.name like %:kw% or h2.name like %:kw%)) "
            + "or (:target = 'hashtag' and (h.name like %:kw% or h2.name like %:kw%)) "
            + "or (:target = 'name' and s.name like %:kw%))"
            , nativeQuery = true)
    int countByRegionAndCategoryIdAndKwAndTarget(@Param("region") String region,
                                                 @Param("category_id") Long categoryId,
                                                 @Param("kw") String kw,
                                                 @Param("target") String target);
}
