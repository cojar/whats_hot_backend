package com.cojar.whats_hot_backend.domain.category.service;

import com.cojar.whats_hot_backend.domain.category.entity.Category;
import com.cojar.whats_hot_backend.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public boolean hasNoCategory() {
        return this.categoryRepository.count() == 0;
    }

    public Category create(String name, Integer depth, Long parent_Id) {

        Category parent = this.categoryRepository.findById(parent_Id)
                .orElse(null);

        Category category = Category.builder()
                .name(name)
                .depth(depth)
                .parent(parent)
                .build();

        this.categoryRepository.save(category);

        return category;
    }

    public Category getCategoryByName(String name) {
        return this.categoryRepository.findByName(name).orElse(null);
    }
}
