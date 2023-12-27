package com.cojar.whats_hot_backend.domain.spot_module.spot.controller;

import com.cojar.whats_hot_backend.domain.base_module.file.entity.FileDomain;
import com.cojar.whats_hot_backend.domain.base_module.file.entity._File;
import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.entity.MenuItem;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.service.MenuItemService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.api_response.SpotApiResponse;
import com.cojar.whats_hot_backend.domain.spot_module.spot.dto.SpotDto;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.request.SpotRequest;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.entity.SpotHashtag;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.service.SpotHashtagService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.entity.SpotImage;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.service.SpotImageService;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.PagedDataModel;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@Tag(name = "Spot", description = "장소 서비스 API")
@RequestMapping(value = "/api/spots", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class SpotController {

    private final SpotService spotService;
    private final CategoryService categoryService;
    private final SpotHashtagService spotHashtagService;
    private final MenuItemService menuItemService;
    private final FileService fileService;
    private final SpotImageService spotImageService;

    @SpotApiResponse.Create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createSpot(@Valid @RequestPart(value = "request") SpotRequest.CreateSpot request, Errors errors,
                                     @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        log.info("=== controller start ===");

        Spot spot = this.spotService.create(request, images, errors);
        Spot createdSpot = this.spotService.getSpotById(spot.getId());
        log.info("Created Spot: {}", createdSpot);

        ResData resData = ResData.of(
                ResCode.S_02_01,
                SpotDto.of(spot),
                linkTo(this.getClass()).slash(spot.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Spot/create").withRel("profile"));

        return ResponseEntity.created(resData.getSelfUri())
                .body(resData);
    }

    @SpotApiResponse.List
    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity list(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "2") int size) {

        Page<DataModel> spotList = this.spotService.getSpotList(page, size);

        ResData resData = ResData.of(
                ResCode.S_02_02,
                PagedDataModel.of(spotList),
                linkTo(this.getClass()).slash("?page=%s&size=%s".formatted(page, size))
        );

        // TODO: paged links with query; custom method
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Spot/list").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @SpotApiResponse.Detail
    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity detail(@PathVariable(value = "id") Long id) {

        Spot spot = this.spotService.getSpotById(id);

        ResData resData = ResData.of(
                ResCode.S_02_03,
                SpotDto.of(spot),
                linkTo(this.getClass()).slash(spot.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Spot/detail").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }

    @SpotApiResponse.Update
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateSpot(@Valid @RequestPart(value = "request", required = false) SpotRequest.UpdateSpot request, Errors errors,
                                     @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                     @PathVariable(value = "id") Long id) {

        ResData resData = this.spotService.updateValidate(id, request, errors);
        if (resData != null) return ResponseEntity.badRequest().body(resData);

        Spot spot = this.spotService.getSpotById(id);

        spot = this.spotService.update(spot, request);

        // hashtags 수정
        List<SpotHashtag> oldSpotHashtags = spot.getHashtags();
        List<SpotHashtag> newSpotHashtags = null;
        if (request.getHashtags() != null) {
            newSpotHashtags = this.spotHashtagService.createAll(request.getHashtags(), spot);
            spot = this.spotService.updateHashtags(spot, newSpotHashtags);
        }

        // menu item 수정
        List<MenuItem> oldMenuItems = spot.getMenuItems();
        List<MenuItem> newMenuItems = null;
        if (request.getMenuItems() != null) {
            newMenuItems = this.menuItemService.createAll(request.getMenuItems(), spot);
            spot = this.spotService.updateMenuItems(spot, newMenuItems);
        }

        // images 수정
        List<_File> newFiles = null;
        List<SpotImage> oldSpotImages = spot.getImages();
        List<SpotImage> newSpotImages = null;
        if (images != null) {
            resData = this.fileService.validateAll(images);
            if (resData != null) return ResponseEntity.badRequest().body(resData);
            newFiles = this.fileService.createAll(images, FileDomain.SPOT);
            newSpotImages = this.spotImageService.createAll(newFiles, spot);
            spot = this.spotService.updateImages(spot, newSpotImages);
        }

        // 중간에 Fail, Exception 발생 시 실제 DB에 저장되지 않도록 나중에 저장
        this.spotHashtagService.saveAll(newSpotHashtags, oldSpotHashtags);
        this.menuItemService.saveAll(newMenuItems, oldMenuItems);
        this.fileService.saveAll(newFiles);
        this.spotImageService.saveAll(newSpotImages, oldSpotImages);
        this.spotService.save(spot);

        resData = ResData.of(
                ResCode.S_02_04,
                SpotDto.of(spot),
                linkTo(this.getClass()).slash(spot.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Spot/update").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }

    @SpotApiResponse.Delete
    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity delete(@PathVariable(value = "id") Long id) {

        ResData resData = ResData.of(
                ResCode.S_02_05,
                linkTo(this.getClass())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Spot/delete").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }
}
