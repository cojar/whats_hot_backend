package com.cojar.whats_hot_backend.domain.spot_module.spot.service;

import com.cojar.whats_hot_backend.domain.spot_module.category.entity.Category;
import com.cojar.whats_hot_backend.domain.spot_module.category.repository.CategoryRepository;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.entity.MenuItem;
import com.cojar.whats_hot_backend.domain.spot_module.spot.controller.SpotController;
import com.cojar.whats_hot_backend.domain.spot_module.spot.dto.SpotListDto;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.repository.SpotRepository;
import com.cojar.whats_hot_backend.domain.spot_module.spot.request.SpotRequest;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.entity.SpotHashtag;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.entity.SpotImage;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SpotService {

    private final SpotRepository spotRepository;
    private final CategoryRepository categoryRepository;

    public ResData createValidate(SpotRequest.CreateSpot request, Errors errors) {

        if (errors.hasErrors()) {
            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-02-01-01",
                    "요청 값이 올바르지 않습니다",
                    errors
            );
        }

        Category category = this.categoryRepository.findById(request.getCategoryId())
                .orElse(null);

        if (category == null) {

            errors.rejectValue("categoryId", "not exist", "category that has request id does not exist");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-02-01-02",
                    "존재하지 않는 카테고리입니다",
                    errors
            );
        }

        if (category.getDepth() != 3) {

            errors.rejectValue("categoryId", "invalid", "category that has request id is invalid");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-02-01-03",
                    "소분류 카테고리 아이디를 입력해주세요",
                    errors
            );
        }

        if (this.spotRepository.existsByNameAndAddress(request.getName(), request.getAddress())) {

            errors.reject("duplicated", new Object[]{request.getName(), request.getAddress()}, "spot that has same name and same address is already exist");

            System.out.println(errors);

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-02-01-04",
                    "같은 이름과 주소를 가진 장소가 이미 존재합니다",
                    errors
            );
        }

        return null;
    }

    public Spot create(SpotRequest.CreateSpot request) {

        Spot spot = Spot.builder()
                .category(this.categoryRepository.findById(request.getCategoryId()).orElse(null))
                .name(request.getName())
                .address(request.getAddress())
                .contact(request.getContact())
                .build();

        return spot;
    }

    @Transactional
    public void save(Spot spot) {
        this.spotRepository.save(spot);
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

    public ResData updateValidate(Long id, SpotRequest.UpdateSpot request, Errors errors) {

        if (!this.spotRepository.existsById(id)) {

            errors.reject("not exist", new Object[]{id}, "spot that has id does not exist");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-02-04-01",
                    "해당 아이디를 가진 장소가 존재하지 않습니다",
                    errors
            );
        }

        if (errors.hasErrors()) {
            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-02-04-02",
                    "요청 값이 올바르지 않습니다",
                    errors
            );
        }

        Category category = this.categoryRepository.findById(request.getCategoryId())
                .orElse(null);

        if (category == null) {

            errors.rejectValue("categoryId", "not exist", "category that has request id does not exist");

            return ResData.of(
                    HttpStatus.BAD_REQUEST,
                    "F-02-04-03",
                    "존재하지 않는 카테고리입니다",
                    errors
            );
        }

        return null;
    }

    public Spot update(Spot spot, SpotRequest.UpdateSpot request) {

        spot = spot.toBuilder()
                .category(request.getCategoryId() != null ?
                        this.categoryRepository.findById(request.getCategoryId()).orElse(null) : spot.getCategory())
                .name(request.getName() != null ? request.getName() : spot.getName())
                .address(request.getAddress() != null ? request.getAddress() : spot.getAddress())
                .contact(request.getContact() != null ? request.getContact() : spot.getContact())
                .build();

        return spot;
    }

    public Spot updateHashtags(Spot spot, List<SpotHashtag> spotHashtags) {
        return spot.toBuilder()
                .hashtags(spotHashtags)
                .build();
    }

    public Spot updateMenuItems(Spot spot, List<MenuItem> menuItems) {
        return spot.toBuilder()
                .menuItems(menuItems)
                .build();
    }

    public Spot updateImages(Spot spot, List<SpotImage> spotImages) {
        return spot.toBuilder()
                .images(spotImages)
                .build();
    }
}
