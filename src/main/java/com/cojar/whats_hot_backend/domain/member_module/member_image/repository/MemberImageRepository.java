package com.cojar.whats_hot_backend.domain.member_module.member_image.repository;

import com.cojar.whats_hot_backend.domain.member_module.member_image.entity.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {
}
