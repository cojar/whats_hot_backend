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
    public Category create(String name, Long parentId, Boolean allowRoot) {

        Category parent = this.categoryRepository.findById(parentId == null ? -1 : parentId)
                .orElse(null);

        createValidate(name, parentId, allowRoot, parent);

        Category category = Category.builder()
                .name(name)
                .depth(parent == null ? 1 : parent.getDepth()+1)
                .parent(parent)
                .build();

        this.categoryRepository.save(category);

        return category;
    }


    private void createValidate(String name, Long parentId, Boolean allowRoot, Category parent) {

        // 첫 카테고리 등록인데 parent 가 있는 경우
        if((parent != null && allowRoot)) {
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_05_01_01
                    )
            );
        }

        // 허용되지 않은 1차 카테고리 등록시
        if(!allowRoot && parentId == null){
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_05_01_02
                    )
            );
        }

        // 하위 카테고리 등록인데 상위 카테고리가 없는 경우
        if((parent == null && (allowRoot == null || !allowRoot))){
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_05_01_03
                    )
            );
        }


        // 하위 카테고리 등록인데 depth가 4가 넘는 경우
        if((parent != null && (parent.getDepth() + 1 >= 4))){
            throw new ApiResponseException(
                ResData.of(
                        ResCode.F_05_01_04
                )
            );
        }

        // 맛집 카테고리가 아닌데 3차 카테고리인 경우
        if((parent !=null && (parent.getDepth() + 1 == 3) && !parent.getRootName().equals("맛집"))){
            throw new ApiResponseException(
                ResData.of(
                        ResCode.F_05_01_04
                )
            );
        }
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
