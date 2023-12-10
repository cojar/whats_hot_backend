package com.cojar.whats_hot_backend.domain.spot.controller;

import com.cojar.whats_hot_backend.domain.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot.dto.SpotDto;
import com.cojar.whats_hot_backend.domain.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot.request.SpotRequest;
import com.cojar.whats_hot_backend.domain.spot.response.SpotResponse;
import com.cojar.whats_hot_backend.domain.spot.service.SpotService;
import com.cojar.whats_hot_backend.domain.spot.api_response.SpotApiResponse;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
                new SpotResponse.Create(SpotDto.of(spot)),
                linkTo(this.getClass()).slash(spot.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Spot/create").withRel("profile"));

        return ResponseEntity.created(resData.getSelfUri())
                .body(resData);
    }
}
