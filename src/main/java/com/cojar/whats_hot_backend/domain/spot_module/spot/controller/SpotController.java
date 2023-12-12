package com.cojar.whats_hot_backend.domain.spot_module.spot.controller;

import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.api_response.SpotApiResponse;
import com.cojar.whats_hot_backend.domain.spot_module.spot.dto.SpotDto;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.request.SpotRequest;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.PagedDataModel;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Tag(name = "Spot", description = "장소 서비스 API")
@RequestMapping(value = "/api/spots", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class SpotController {

    private final SpotService spotService;
    private final CategoryService categoryService;

    @SpotApiResponse.Create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity create(@Valid @RequestPart(value = "createReq") SpotRequest.Create createReq, Errors errors,
                                 @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        Spot spot = this.spotService.getSpotById(1L);

        ResData resData = ResData.of(
                HttpStatus.CREATED,
                "S-02-01",
                "장소 등록이 완료되었습니다",
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
                HttpStatus.OK,
                "S-02-02",
                "요청하신 장소 목록을 반환합니다",
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
                HttpStatus.OK,
                "S-02-03",
                "요청하신 장소 정보를 반환합니다",
                SpotDto.of(spot),
                linkTo(this.getClass()).slash(spot.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Spot/detail").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }

    @SpotApiResponse.Update
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity update(@Valid @RequestPart(value = "updateReq") SpotRequest.Update updateReq, Errors errors,
                                 @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                 @PathVariable(value = "id") Long id) {

        Spot spot = this.spotService.getSpotById(1L);

        ResData resData = ResData.of(
                HttpStatus.OK,
                "S-02-04",
                "장소 수정이 완료되었습니다",
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
                HttpStatus.OK,
                "S-02-05",
                "장소 삭제가 완료되었습니다",
                linkTo(this.getClass())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Spot/delete").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }
}
