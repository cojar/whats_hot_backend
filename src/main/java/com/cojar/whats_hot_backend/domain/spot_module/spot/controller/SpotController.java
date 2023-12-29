package com.cojar.whats_hot_backend.domain.spot_module.spot.controller;

import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.service.MenuItemService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.api_response.SpotApiResponse;
import com.cojar.whats_hot_backend.domain.spot_module.spot.dto.SpotDto;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.request.SpotRequest;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.service.SpotHashtagService;
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

        Spot spot = this.spotService.create(request, images, errors);

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
                               @RequestParam(value = "size", defaultValue = "2") int size,
                               @RequestParam(value = "kw", defaultValue = "") String kw) {

        Page<DataModel> spotList = this.spotService.getSpotList(page, size, kw);


        ResData resData = ResData.of(
                ResCode.S_02_02,
                PagedDataModel.of(spotList),
                linkTo(this.getClass()).slash("?page=%s&size=%s&kw=%s".formatted(page, size, kw))
        );

        // TODO: paged links with query; custom method
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Spot/list").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @SpotApiResponse.Detail
    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity detail(@PathVariable(value = "id") Long id) {

        ResData resData = this.spotService.getSpotValidate(id);

        if (resData != null) return ResponseEntity.badRequest().body(resData);

        Spot spot = this.spotService.getSpotById(id);


        resData = ResData.of(
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

        Spot spot = this.spotService.update(id, request, images, errors);

        ResData resData = ResData.of(
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
    public ResponseEntity deleteSpot(@PathVariable(value = "id") Long id) {

        this.spotService.delete(id);

        ResData resData = ResData.of(
                ResCode.S_02_05,
                linkTo(this.getClass())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Spot/delete").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }
}
