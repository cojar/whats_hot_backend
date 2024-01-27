package com.cojar.whats_hot_backend.domain.spot_module.spot.controller;

import com.cojar.whats_hot_backend.domain.member_module.member.entity.Member;
import com.cojar.whats_hot_backend.domain.member_module.member.service.MemberService;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.api_response.SpotApiResponse;
import com.cojar.whats_hot_backend.domain.spot_module.spot.dto.SpotDto;
import com.cojar.whats_hot_backend.domain.spot_module.spot.dto.SpotStarDto;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.request.SpotRequest;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.global.response.DataModel;
import com.cojar.whats_hot_backend.global.response.PagedDataModel;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@Tag(name = "Spot", description = "장소 서비스 API")
@RequestMapping(value = "/api/spots", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class SpotController {

    private final SpotService spotService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    @SpotApiResponse.Create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createSpot(@Valid @RequestPart(value = "request") SpotRequest.CreateSpot request, Errors errors,
                                     @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                     @AuthenticationPrincipal User user) {

        Member member = this.memberService.getUserByUsername(user.getUsername());
        Spot spot = this.spotService.create(request, images, errors);
        Page<DataModel> reviewPages = this.reviewService.getReviewPagesWithoutValidate(1, 20, "like", spot.getId(), false, member);

        ResData resData = ResData.of(
                ResCode.S_02_01,
                SpotDto.of(spot, member, PagedDataModel.of(reviewPages)),
                linkTo(this.getClass()).slash(spot.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/swagger-ui/index.html#/Spot/create").withRel("profile"));

        return ResponseEntity.created(resData.getSelfUri())
                .body(resData);
    }

    @SpotApiResponse.List
    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity getSpots(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "size", defaultValue = "5") int size,
                                   @RequestParam(value = "region", defaultValue = "") String region,
                                   @RequestParam(value = "categoryId", defaultValue = "-1") Long categoryId,
                                   @RequestParam(value = "sort", defaultValue = "score") String sort,
                                   @RequestParam(value = "kw", defaultValue = "") String kw,
                                   @RequestParam(value = "target", defaultValue = "all") String target,
                                   @AuthenticationPrincipal User user,
                                   HttpServletRequest request) throws UnsupportedEncodingException {

        Member member = user != null ? this.memberService.getUserByUsername(user.getUsername()) : null;
        Page<DataModel> spotList = this.spotService.getSpotPages(page, size, region, categoryId, sort, kw, target, member);

        ResData resData = ResData.of(
                ResCode.S_02_02,
                PagedDataModel.of(spotList),
                linkTo(this.getClass()).slash(request.getQueryString() != null ? "?%s".formatted(URLDecoder.decode(request.getQueryString(), "UTF-8")) : "")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/api/swagger-ui/index.html#/Spot/list").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }

    @SpotApiResponse.Detail
    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity getSpot(@PathVariable(value = "id") Long id,
                                  @AuthenticationPrincipal User user) {

        Member member = user != null ? this.memberService.getUserByUsername(user.getUsername()) : null;
        Spot spot = this.spotService.getSpotById(id);
        Page<DataModel> reviewPages = this.reviewService.getReviewPagesWithoutValidate(1, 5, "like", spot.getId(), false, member);

        ResData resData = ResData.of(
                ResCode.S_02_03,
                SpotDto.of(spot, member, PagedDataModel.of(reviewPages)),
                linkTo(this.getClass()).slash(spot.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/api/swagger-ui/index.html#/Spot/getSpot").withRel("profile"));

        return ResponseEntity.ok()
                .body(resData);
    }

    @SpotApiResponse.Update
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateSpot(@Valid @RequestPart(value = "request", required = false) SpotRequest.UpdateSpot request, Errors errors,
                                     @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                     @PathVariable(value = "id") Long id,
                                     @AuthenticationPrincipal User user) {

        Member member = this.memberService.getUserByUsername(user.getUsername());
        Spot spot = this.spotService.update(id, request, images, errors);
        Page<DataModel> reviewPages = this.reviewService.getReviewPagesWithoutValidate(1, 20, "like", spot.getId(), false, member);

        ResData resData = ResData.of(
                ResCode.S_02_04,
                SpotDto.of(spot, member, PagedDataModel.of(reviewPages)),
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

    @SpotApiResponse.Star
    @PatchMapping(value = "/{id}/star", consumes = MediaType.ALL_VALUE)
    public ResponseEntity starSpot(@PathVariable(value = "id") Long id,
                                   @AuthenticationPrincipal User user) {

        Member member = this.memberService.getUserByUsername(user.getUsername());
        Spot spot = this.spotService.toggleStar(id, member);

        ResData resData = ResData.of(
                ResCode.S_02_06,
                SpotStarDto.of(spot, member),
                linkTo(this.getClass()).slash(spot.getId())
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/api/swagger-ui/index.html#/Spot/starSpot").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }
}
