package com.cojar.whats_hot_backend.domain.spot_module.category.controller;

import com.cojar.whats_hot_backend.domain.spot_module.category.api_response.CategoryApiResponse;
import com.cojar.whats_hot_backend.domain.spot_module.category.dto.CategoryDto;
import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.category.request.CategoryRequest;
import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.global.response.*;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@Tag(name = "Category", description = "장소 카테고리 API")
@RequestMapping(value = "/api/categories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    //    @CategoryApiResponse.Create
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest.CreateCategory request, Errors errors) {
        // 유효성 검사
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid request body");
        }
        Category category = this.categoryService.create(request.getName(), request.getDepth(), request.getParentId());

        ResData resData = ResData.of(
                ResCode.S_05_01,
                CategoryDto.of(category),
                linkTo(CategoryController.class).slash("")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Review/createReview").withRel("profile"));
        return ResponseEntity.created(resData.getSelfUri())
                .body(resData);
    }

    @CategoryApiResponse.List
    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity getCategories(@RequestParam(value = "parentId", defaultValue = "-1") Long parentId,
                                        HttpServletRequest request) {

        List<DataModel> categories = this.categoryService.getCategoriesByParent(parentId);

        ResData resData = ResData.of(
                ResCode.S_05_02,
                ListDataModel.of(categories),
                linkTo(this.getClass()).slash(request.getQueryString() != null ? "?%s".formatted(request.getQueryString()) : "")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/api/swagger-ui/index.html#Category/getCategories").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

}
