package com.cojar.whats_hot_backend.domain.spot_module.category.service;

import com.cojar.whats_hot_backend.domain.spot_module.category.controller.CategoryController;
import com.cojar.whats_hot_backend.domain.spot_module.category.dto.CategoryListDto;
import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.category.repository.CategoryRepository;
import com.cojar.whats_hot_backend.global.response.DataModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category create(String name, Integer depth, Long parentId) {

        Category parent = this.categoryRepository.findById(parentId == null ? -1 : parentId)
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
        if (id == null) return null;
        return this.categoryRepository.findById(id).orElse(null);
    }

    public List<Category> getTreeList(Long categoryId) {

        Category category = this.getCategoryById(categoryId);

        List<Category> categories = new ArrayList<>();

        while(category != null) {
            categories.add(category);
            category = category.getParent();
        }

        return categories;
    }

    public List<DataModel> getCategoriesByParent(Long parentId) {

        if (parentId == -1) return this.categoryRepository.findAllByDepth(1).stream()
                .map(category -> {
                    DataModel dataModel = DataModel.of(
                            CategoryListDto.of(category),
                            linkTo(CategoryController.class).slash(category.getId())
                    );
                    return dataModel;
                })
                .collect(Collectors.toList());

        return this.getCategoryById(parentId).getChildren().stream()
                .map(category -> {
                    DataModel dataModel = DataModel.of(
                            CategoryListDto.of(category),
                            linkTo(CategoryController.class).slash(category.getId())
                    );
                    return dataModel;
                })
                .collect(Collectors.toList());
    }
}
