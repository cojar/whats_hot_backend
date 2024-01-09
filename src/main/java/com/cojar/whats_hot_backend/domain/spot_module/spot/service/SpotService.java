package com.cojar.whats_hot_backend.domain.spot_module.spot.service;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.base_module.hashtag.entity.Hashtag;
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
import com.cojar.whats_hot_backend.domain.spot_module.spot_category.service.SpotCategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.service.SpotHashtagService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.service.SpotImageService;
import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final SpotCategoryService spotCategoryService;

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

        Spot spot = Spot.builder()
                .name(request.getName())
                .address(request.getAddress())
                .contact(request.getContact())
                .build();

        this.spotRepository.save(spot);

        // categories 생성
        this.spotCategoryService.createAll(request.getCategoryId(), spot);

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

        if ((category.getRootName().equals("맛집") && category.getDepth() != 3) || (category.getRootName().equals("여행지") && category.getDepth() != 2)
                || (category.getRootName().equals("숙박") && category.getDepth() != 2)) {

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

    public Page<DataModel> getSpotList(int page, int size, String kw) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Specification<Spot> spec = search(kw);

        return this.spotRepository.findAll(pageable)
                .map(spot -> {
                    DataModel dataModel = DataModel.of(
                            SpotListDto.of(spot),
                            linkTo(SpotController.class).slash(spot.getId())
                    );
                    return dataModel;
                });
    }


    private Specification<Spot> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Spot> s, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Spot, Hashtag> h = s.join("hashtags", JoinType.LEFT);
                Join<Spot, Category> c = s.join("category", JoinType.LEFT);
                return cb.or(cb.like(s.get("name"), "%" + kw + "%"), // 장소이름
                        cb.like(h.get("name"), "%" + kw + "%"),      // 해쉬태그
                        cb.like(c.get("name"), "%" + kw + "%"));   // 카테고리
                // 내추럴 쿼리 ,
            }
        };
    }


    @Transactional
    public Spot update(Long id, SpotRequest.UpdateSpot request, List<MultipartFile> images, Errors errors) {

        // request 에러 검증
        this.updateValidate(id, request, errors);

        // images 에러 검증
        this.fileService.validateAll(images);

        // 검증 단계에서 에러 걸러짐
        Spot spot = this.getSpotById(id);

        spot = spot.toBuilder()
                .name(request.getName() != null ? request.getName() : spot.getName())
                .address(request.getAddress() != null ? request.getAddress() : spot.getAddress())
                .contact(request.getContact() != null ? request.getContact() : spot.getContact())
                .build();

        spot = this.spotRepository.save(spot);

        // categories 생성
        this.spotCategoryService.updateAll(request.getCategoryId(), spot);

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

                throw new ApiResponseException(
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

        // id 에러 걸러짐
        this.deleteValidate(id);

        // 검증 이후이므로 null 아님
        Spot spot = this.getSpotById(id);

        List<_File> files = spot.getImages().stream()
                .map(image -> image.getImage())
                .collect(Collectors.toList());
        this.fileService.deleteFile(files);

        this.spotRepository.delete(spot);
    }

    private void deleteValidate(Long id) {

        Errors errors = AppConfig.getMockErrors("spot");

        if (!this.spotRepository.existsById(id)) {
            errors.reject("not exist", new Object[]{id}, "spot that has id does not exist");

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_02_05_01,
                            errors
                    )
            );
        }
    }

    @Transactional
    public void updateReview(Spot spot, Review review) {

        List<Review> reviews = spot.getReviews();
        reviews.add(review);
        Double averageScore = (spot.getAverageScore() + review.getScore()) / reviews.size();

        spot = spot.toBuilder()
                .averageScore(averageScore)
                .reviews(reviews)
                .build();

        this.spotRepository.save(spot);
    }

    public ResData getSpotValidate(Long spotid) {
        Errors errors = AppConfig.getMockErrors("spot");

        errors.reject("not exist", new Object[]{spotid}, "Spot that has id does not exist");
        if (!this.spotRepository.existsById(spotid)) {

            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_02_04_01,
                            errors
                    )
            );
        }
        return null;
    }

}
