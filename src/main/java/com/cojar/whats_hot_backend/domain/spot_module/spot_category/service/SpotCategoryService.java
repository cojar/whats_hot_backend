package com.cojar.whats_hot_backend.domain.spot_module.spot_category.service;

import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot_category.entity.SpotCategory;
import com.cojar.whats_hot_backend.domain.spot_module.spot_category.repository.SpotCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SpotCategoryService {

    private final SpotCategoryRepository spotCategoryRepository;

    private final CategoryService categoryService;

    @Transactional
    public List<SpotCategory> createAll(Long categoryId, Spot spot) {

        List<SpotCategory> spotCategories = this.categoryService.getTreeList(categoryId).stream()
                .map(category -> SpotCategory.builder()
                        .category(category)
                        .spot(spot)
                        .build()
                ).collect(Collectors.toList());

        this.spotCategoryRepository.saveAll(spotCategories);

        return spotCategories;
    }

    @Transactional
    public List<SpotCategory> updateAll(Long categoryId, Spot spot) {

        if (categoryId == null) return null;

        List<SpotCategory> spotCategories = this.categoryService.getTreeList(categoryId).stream()
                .map(category -> SpotCategory.builder()
                        .category(category)
                        .spot(spot)
                        .build()
                ).collect(Collectors.toList());

        this.spotCategoryRepository.deleteAll(spot.getCategories());
        this.spotCategoryRepository.saveAll(spotCategories);

        return spotCategories;
    }
}
