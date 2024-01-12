package com.cojar.whats_hot_backend.domain.spot_module.category.service;

import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.category.repository.CategoryRepository;
import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category create(String name, Integer depth, Long parentId) {
        // 1차 카테고리는 만들지 않고 4차부터도 만들지 않음
        if(depth == 1 || depth >= 4){
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_05_01_01
                    )
            );
        }

        Category parent = this.categoryRepository.findById(parentId == null ? -1 : parentId)
                .orElse(null);

        // 최상단 카테고리가 맛집일 경우 depth 3, 나머지는 2까지 생성 가능
        if( depth == 3 && !parent.getRootName().equals("맛집")){
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_05_01_01
                    )
            );
        }

        Category category = Category.builder()
                .name(name)
                .depth(depth)
                .parent(parent)
                .build();

        this.categoryRepository.save(category);

        return category;
    }

    // InitConfig용
    @Transactional
    public Category createForInit(String name, Integer depth, Long parentId) {

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
}
