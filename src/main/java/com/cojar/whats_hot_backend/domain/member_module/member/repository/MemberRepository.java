package com.cojar.whats_hot_backend.domain.member_module.member.repository;

import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);
}
