package com.cojar.whats_hot_backend.domain.spot_module.category.service;

import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
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

    public Category getCategoryById(Long id) {
        return this.categoryRepository.findById(id).orElse(null);
    }
}
