package com.cojar.whats_hot_backend.domain.spot_module.spot.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.category.repository.CategoryRepository;
import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.service.MenuItemService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.controller.SpotController;
import com.cojar.whats_hot_backend.domain.spot_module.spot.dto.SpotListDto;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.repository.SpotRepository;
import com.cojar.whats_hot_backend.domain.spot_module.spot.request.SpotRequest;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.service.SpotHashtagService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.service.SpotImageService;
import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SpotService {

    private final SpotRepository spotRepository;
    private final CategoryRepository categoryRepository;

    private final FileService fileService;
    private final CategoryService categoryService;
    private final SpotHashtagService spotHashtagService;
    private final MenuItemService menuItemService;
    private final SpotImageService spotImageService;

    private final EntityManager entityManager;

    public long count() {
        return this.spotRepository.count();
    }

    @Transactional
    public Spot create(SpotRequest.CreateSpot request, List<MultipartFile> images, Errors errors) {

        // request 에러 검증
        this.createValidate(request, errors);

        // images 에러 검증
        this.fileService.validateAll(images);

        // 검증 단계에서 에러 걸러짐
        Category category = this.categoryService.getCategoryById(request.getCategoryId());

        Spot spot = Spot.builder()
                .category(category)
                .name(request.getName())
                .address(request.getAddress())
                .contact(request.getContact())
                .build();

        this.spotRepository.save(spot);

        // hashtags 생성
        this.spotHashtagService.createAll(request.getHashtags(), spot);

        // menu item 생성
        this.menuItemService.createAll(request.getMenuItems(), spot);

        // images 생성
        List<_File> files = this.fileService.createAll(images, FileDomain.SPOT);
        this.spotImageService.createAll(files, spot);

        entityManager.refresh(spot);

        return spot;
    }

    private void createValidate(SpotRequest.CreateSpot request, Errors errors) {

        if (errors.hasErrors()) {
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_02_01_01,
                            errors
                    )
            );
        }

        Category category = this.categoryRepository.findById(request.getCategoryId())
                .orElse(null);

        if (category == null) {

            errors.rejectValue("categoryId", "not exist", "category that has request id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_02_01_02,
                            errors
                    )
            );
        }

        if (category.getDepth() != 3) {

            errors.rejectValue("categoryId", "invalid", "category that has request id is invalid");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_02_01_03,
                            errors
                    )
            );
        }

        if (this.spotRepository.existsByNameAndAddress(request.getName(), request.getAddress())) {

            errors.reject("duplicated", new Object[]{request.getName(), request.getAddress()}, "spot that has same name and same address is already exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_02_01_04,
                            errors
                    )
            );
        }
    }

    public Spot getSpotById(Long id) {
        return this.spotRepository.findById(id)
                .orElse(null);
    }

    public Page<DataModel> getSpotList(int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return this.spotRepository.findAll(pageable)
                .map(spot -> {
                    DataModel dataModel = DataModel.of(
                            SpotListDto.of(spot),
                            linkTo(SpotController.class).slash(spot.getId())
                    );
                    return dataModel;
                });
    }

    @Transactional
    public Spot update(Long id, SpotRequest.UpdateSpot request, List<MultipartFile> images, Errors errors) {

        // request 에러 검증
        this.updateValidate(id, request, errors);

        // images 에러 검증
        this.fileService.validateAll(images);

        // 검증 단계에서 에러 걸러짐
        Spot spot = this.getSpotById(id);

        Category category = this.categoryService.getCategoryById(request.getCategoryId());

        spot = spot.toBuilder()
                .category(category != null ? category : spot.getCategory())
                .name(request.getName() != null ? request.getName() : spot.getName())
                .address(request.getAddress() != null ? request.getAddress() : spot.getAddress())
                .contact(request.getContact() != null ? request.getContact() : spot.getContact())
                .build();

        spot = this.spotRepository.save(spot);

        // hashtags 수정
        this.spotHashtagService.updateAll(request.getHashtags(), spot);

        // menu item 수정
        this.menuItemService.updateAll(request.getMenuItems(), spot);

        // images 수정
        List<_File> files = this.fileService.createAll(images, FileDomain.SPOT);
        this.spotImageService.updateAll(files, spot);

        entityManager.flush();
        entityManager.refresh(spot);

        return spot;
    }

    private void updateValidate(Long id, SpotRequest.UpdateSpot request, Errors errors) {

        if (!this.spotRepository.existsById(id)) {

            errors.reject("not exist", new Object[]{id}, "spot that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_02_04_01,
                            errors
                    )
            );
        }

        if (errors.hasErrors()) {
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_02_04_02,
                            errors
                    )
            );
        }

        if (request.getCategoryId() != null) {

            Category category = this.categoryRepository.findById(request.getCategoryId())
                    .orElse(null);

            if (category == null) {

                errors.rejectValue("categoryId", "not exist", "category that has request id does not exist");

                throw new  ApiResponseException(
                        ResData.of(
                                ResCode.F_02_04_03,
                                errors
                        )
                );
            }

            if (category.getDepth() != 3) {

                errors.rejectValue("categoryId", "invalid", "category that has request id is invalid");

                throw new ApiResponseException(
                        ResData.of(
                                ResCode.F_02_04_04,
                                errors
                        )
                );
            }
        }

        if (this.spotRepository.existsByNameAndAddress(request.getName(), request.getAddress())
                && this.spotRepository.findByNameAndAddress(request.getName(), request.getAddress()).orElse(null).getId() != id) {

            errors.reject("duplicated", new Object[]{request.getName(), request.getAddress()}, "spot that has same name and same address is already exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_02_04_05,
                            errors
                    )
            );
        }
    }

    @Transactional
    public void delete(Long id) {

        Spot spot = this.getSpotById(id);

        List<_File> files = spot.getImages().stream()
                .map(image -> image.getImage())
                .collect(Collectors.toList());
        this.fileService.deleteFile(files);

        this.spotRepository.delete(spot);
    }

    public Spot updateReview(Spot spot, Review review) {

        List<Review> reviews = spot.getReviews();
        reviews.add(review);
        Double averageScore = (spot.getAverageScore() + review.getScore()) / reviews.size();

        return spot.toBuilder()
                .averageScore(averageScore)
                .reviews(reviews)
                .build();
    }
}
