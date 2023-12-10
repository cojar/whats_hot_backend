package com.cojar.whats_hot_backend.domain.category.repository;

import com.cojar.whats_hot_backend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
