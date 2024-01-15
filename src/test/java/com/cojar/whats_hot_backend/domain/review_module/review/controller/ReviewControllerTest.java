package com.cojar.whats_hot_backend.domain.review_module.review.controller;

import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.Review;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.ReviewStatus;
import com.cojar.whats_hot_backend.domain.review_module.review.repository.ReviewRepository;
import com.cojar.whats_hot_backend.domain.review_module.review.request.ReviewRequest;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.entity.ReviewHashtag;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.service.ReviewHashtagService;
import com.cojar.whats_hot_backend.domain.review_module.review_image.entity.ReviewImage;
import com.cojar.whats_hot_backend.domain.review_module.review_image.service.ReviewImageService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.entity.Spot;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;
import com.cojar.whats_hot_backend.global.errors.exception.ApiResponseException;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.response.ResData;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerTest extends BaseControllerTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewHashtagService reviewHashtagService;

    @Autowired
    private ReviewImageService reviewImageService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private SpotService spotService;

    @Transactional
    @Test
    @DisplayName("post:/api/reviews - created, S-03-01")
    public void createReview_Created() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long spotId = 1L;
        Integer year = 2023, month = 12, day = 25;
        String title = "리뷰 제목";
        String content = "리뷰 내용";
        Double score = 4.5;
        String hashtag1 = "해시태그1", hashtag2 = "해시태그2";
        ReviewRequest.CreateReview request = ReviewRequest.CreateReview.builder()
                .spotId(spotId)
                .year(year)
                .month(month)
                .day(day)
                .title(title)
                .content(content)
                .score(score)
                .hashtags(List.of(hashtag1, hashtag2))
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String fileName = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName, ext));
        MockMultipartFile _file1 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );
        MockMultipartFile _file2 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/reviews")
                        .file(_request)
                        .file(_file1)
                        .file(_file2)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-03-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.spot.id").value(spotId))
                .andExpect(jsonPath("data.spot.averageScore").exists())
                .andExpect(jsonPath("data.spot.reviews").exists())
                .andExpect(jsonPath("data.author").value(username))
                .andExpect(jsonPath("data.visitDate").value("%04d-%02d-%02dT00:00:00".formatted(year, month, day)))
                .andExpect(jsonPath("data.title").value(title))
                .andExpect(jsonPath("data.content").value(content))
                .andExpect(jsonPath("data.score").value(score))
                .andExpect(jsonPath("data.hashtags[0]").value(hashtag1))
                .andExpect(jsonPath("data.hashtags[1]").value(hashtag2))
                .andExpect(jsonPath("data.imageUri[0]").exists())
                .andExpect(jsonPath("data.imageUri[1]").exists())
                .andExpect(jsonPath("data.status").value(ReviewStatus.PUBLIC.getType()))
                .andExpect(jsonPath("data.validated").value("false"))
                .andExpect(jsonPath("data.liked").value(0))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Transactional
    @Test
    @DisplayName("post:/api/reviews - created status private, S-03-01")
    public void createReview_Created_StatusPrivate() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long spotId = 1L;
        Integer year = 2023, month = 12, day = 25;
        String title = "리뷰 제목";
        String content = "리뷰 내용";
        Double score = 4.5;
        String hashtag1 = "해시태그1", hashtag2 = "해시태그2";
        boolean lock = true;
        ReviewRequest.CreateReview request = ReviewRequest.CreateReview.builder()
                .spotId(spotId)
                .year(year)
                .month(month)
                .day(day)
                .title(title)
                .content(content)
                .score(score)
                .hashtags(List.of(hashtag1, hashtag2))
                .lock(lock)
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String fileName = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName, ext));
        MockMultipartFile _file1 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );
        MockMultipartFile _file2 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/reviews")
                        .file(_request)
                        .file(_file1)
                        .file(_file2)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-03-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.spot.id").value(spotId))
                .andExpect(jsonPath("data.spot.averageScore").exists())
                .andExpect(jsonPath("data.spot.reviews").exists())
                .andExpect(jsonPath("data.author").value(username))
                .andExpect(jsonPath("data.visitDate").value("%04d-%02d-%02dT00:00:00".formatted(year, month, day)))
                .andExpect(jsonPath("data.title").value(title))
                .andExpect(jsonPath("data.content").value(content))
                .andExpect(jsonPath("data.score").value(score))
                .andExpect(jsonPath("data.hashtags[0]").value(hashtag1))
                .andExpect(jsonPath("data.hashtags[1]").value(hashtag2))
                .andExpect(jsonPath("data.imageUri[0]").exists())
                .andExpect(jsonPath("data.imageUri[1]").exists())
                .andExpect(jsonPath("data.status").value(ReviewStatus.PRIVATE.getType()))
                .andExpect(jsonPath("data.validated").value("false"))
                .andExpect(jsonPath("data.liked").value(0))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Transactional
    @Test
    @DisplayName("post:/api/reviews - created without hashtags, S-03-01")
    public void createReview_Created_WithoutHashtags() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long spotId = 1L;
        Integer year = 2023, month = 12, day = 25;
        String title = "리뷰 제목";
        String content = "리뷰 내용";
        Double score = 4.5;
        ReviewRequest.CreateReview request = ReviewRequest.CreateReview.builder()
                .spotId(spotId)
                .year(year)
                .month(month)
                .day(day)
                .title(title)
                .content(content)
                .score(score)
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String fileName = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName, ext));
        MockMultipartFile _file1 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );
        MockMultipartFile _file2 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/reviews")
                        .file(_request)
                        .file(_file1)
                        .file(_file2)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-03-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.spot.id").value(spotId))
                .andExpect(jsonPath("data.spot.averageScore").exists())
                .andExpect(jsonPath("data.spot.reviews").exists())
                .andExpect(jsonPath("data.author").value(username))
                .andExpect(jsonPath("data.visitDate").value("%04d-%02d-%02dT00:00:00".formatted(year, month, day)))
                .andExpect(jsonPath("data.title").value(title))
                .andExpect(jsonPath("data.content").value(content))
                .andExpect(jsonPath("data.score").value(score))
                .andExpect(jsonPath("data.hashtags").doesNotExist())
                .andExpect(jsonPath("data.imageUri[0]").exists())
                .andExpect(jsonPath("data.imageUri[1]").exists())
                .andExpect(jsonPath("data.status").value(ReviewStatus.PUBLIC.getType()))
                .andExpect(jsonPath("data.validated").value("false"))
                .andExpect(jsonPath("data.liked").value(0))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Transactional
    @Test
    @DisplayName("post:/api/reviews - created without images, S-03-01")
    public void createReview_Created_WithoutImages() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long spotId = 1L;
        Integer year = 2023, month = 12, day = 25;
        String title = "리뷰 제목";
        String content = "리뷰 내용";
        Double score = 4.5;
        String hashtag1 = "해시태그1", hashtag2 = "해시태그2";
        ReviewRequest.CreateReview request = ReviewRequest.CreateReview.builder()
                .spotId(spotId)
                .year(year)
                .month(month)
                .day(day)
                .title(title)
                .content(content)
                .score(score)
                .hashtags(List.of(hashtag1, hashtag2))
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/reviews")
                        .file(_request)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-03-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.spot.id").value(spotId))
                .andExpect(jsonPath("data.spot.averageScore").exists())
                .andExpect(jsonPath("data.spot.reviews").exists())
                .andExpect(jsonPath("data.author").value(username))
                .andExpect(jsonPath("data.visitDate").value("%04d-%02d-%02dT00:00:00".formatted(year, month, day)))
                .andExpect(jsonPath("data.title").value(title))
                .andExpect(jsonPath("data.content").value(content))
                .andExpect(jsonPath("data.score").value(score))
                .andExpect(jsonPath("data.hashtags[0]").value(hashtag1))
                .andExpect(jsonPath("data.hashtags[1]").value(hashtag2))
                .andExpect(jsonPath("data.imageUri").doesNotExist())
                .andExpect(jsonPath("data.status").value(ReviewStatus.PUBLIC.getType()))
                .andExpect(jsonPath("data.validated").value("false"))
                .andExpect(jsonPath("data.liked").value(0))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @ParameterizedTest
    @MethodSource("argsFor_createReview_BadRequest_NotBlank")
    @DisplayName("post:/api/reviews - bad request spot not exist, F-03-01-01")
    public void createReview_BadRequest_NotBlank(Long spotId,
                                                 Integer year,
                                                 Integer month,
                                                 Integer day,
                                                 String title,
                                                 String content,
                                                 Double score,
                                                 String hashtag) throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        List<Long> checkList = getCheckListNotCreated();

        ReviewRequest.CreateReview request = ReviewRequest.CreateReview.builder()
                .spotId(spotId)
                .year(year)
                .month(month)
                .day(day)
                .title(title)
                .content(content)
                .score(score)
                .hashtags(List.of(hashtag))
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/reviews")
                        .file(_request)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-03-01-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;

        if (spotId == null || year == null || month == null || day == null || score == null)
            resultActions.andExpect(jsonPath("data[0].rejectedValue").doesNotExist());
        else resultActions.andExpect(jsonPath("data[0].rejectedValue").value(""));

        checkNotCreated(checkList);
    }

    private static Stream<Arguments> argsFor_createReview_BadRequest_NotBlank() {
        return Stream.of(
                Arguments.of(null, 2023, 12, 25, "리뷰 제목", "리뷰 내용", 4.5, "해시태그"),
                Arguments.of(1L, null, 12, 25, "리뷰 제목", "리뷰 내용", 4.5, "해시태그"),
                Arguments.of(1L, 2023, null, 25, "리뷰 제목", "리뷰 내용", 4.5, "해시태그"),
                Arguments.of(1L, 2023, 12, null, "리뷰 제목", "리뷰 내용", 4.5, "해시태그"),
                Arguments.of(1L, 2023, 12, 25, "", "리뷰 내용", 4.5, "해시태그"),
                Arguments.of(1L, 2023, 12, 25, "리뷰 제목", "", 4.5, "해시태그"),
                Arguments.of(1L, 2023, 12, 25, "리뷰 제목", "리뷰 내용", null, "해시태그"),
                Arguments.of(1L, 2023, 12, 25, "리뷰 제목", "리뷰 내용", 4.5, "")
        );
    }

    @Test
    @DisplayName("post:/api/reviews - bad request spot not exist, F-03-01-02")
    public void createReview_BadRequest_SpotNotExist() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        List<Long> checkList = getCheckListNotCreated();

        Long spotId = 1000000000L;
        int year = 2023, month = 12, day = 25;
        String title = "리뷰 제목";
        String content = "리뷰 내용";
        Double score = 4.5;
        String hashtag1 = "해시태그1", hashtag2 = "해시태그2";
        ReviewRequest.CreateReview request = ReviewRequest.CreateReview.builder()
                .spotId(spotId)
                .year(year)
                .month(month)
                .day(day)
                .title(title)
                .content(content)
                .score(score)
                .hashtags(List.of(hashtag1, hashtag2))
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String fileName = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName, ext));
        MockMultipartFile _file1 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );
        MockMultipartFile _file2 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/reviews")
                        .file(_request)
                        .file(_file1)
                        .file(_file2)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-03-01-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(spotId))
                .andExpect(jsonPath("_links.index").exists())
        ;

        checkNotCreated(checkList);
    }

    @Test
    @DisplayName("post:/api/reviews - bad request content type, F-00-00-01")
    public void createReview_BadRequest_ContentType() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        List<Long> checkList = getCheckListNotCreated();

        Long spotId = 1L;
        Integer year = 2023, month = 12, day = 25;
        String title = "리뷰 제목";
        String content = "리뷰 내용";
        Double score = 4.5;
        String hashtag1 = "해시태그1", hashtag2 = "해시태그2";
        ReviewRequest.CreateReview request = ReviewRequest.CreateReview.builder()
                .spotId(spotId)
                .year(year)
                .month(month)
                .day(day)
                .title(title)
                .content(content)
                .score(score)
                .hashtags(List.of(hashtag1, hashtag2))
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String fileName = "test";
        String ext = "png";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName, ext));
        MockMultipartFile _file1 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.TEXT_MARKDOWN_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/reviews")
                        .file(_request)
                        .file(_file1)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code[0]").value("F-00-00-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(MediaType.TEXT_MARKDOWN_VALUE))
        ;

        checkNotCreated(checkList);
    }

    @Test
    @DisplayName("post:/api/reviews - bad request extension, F-00-00-02")
    public void createReview_BadRequest_Extension() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        List<Long> checkList = getCheckListNotCreated();

        Long spotId = 1L;
        Integer year = 2023, month = 12, day = 25;
        String title = "리뷰 제목";
        String content = "리뷰 내용";
        Double score = 4.5;
        String hashtag1 = "해시태그1", hashtag2 = "해시태그2";
        ReviewRequest.CreateReview request = ReviewRequest.CreateReview.builder()
                .spotId(spotId)
                .year(year)
                .month(month)
                .day(day)
                .title(title)
                .content(content)
                .score(score)
                .hashtags(List.of(hashtag1, hashtag2))
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String fileName = "test";
        String ext = "gif";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName, ext));
        MockMultipartFile _file1 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_GIF_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/reviews")
                        .file(_request)
                        .file(_file1)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code[0]").value("F-00-00-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(MediaType.IMAGE_GIF_VALUE))
        ;

        checkNotCreated(checkList);
    }

    @Test
    @DisplayName("post:/api/reviews - bad request multiple file error, F-00-00-01 & F-00-00-02")
    public void createReview_BadRequest_MultipleFileError() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        List<Long> checkList = getCheckListNotCreated();

        Long spotId = 1L;
        Integer year = 2023, month = 12, day = 25;
        String title = "리뷰 제목";
        String content = "리뷰 내용";
        Double score = 4.5;
        String hashtag1 = "해시태그1", hashtag2 = "해시태그2";
        ReviewRequest.CreateReview request = ReviewRequest.CreateReview.builder()
                .spotId(spotId)
                .year(year)
                .month(month)
                .day(day)
                .title(title)
                .content(content)
                .score(score)
                .hashtags(List.of(hashtag1, hashtag2))
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String fileName1 = "test";
        String ext1 = "a";
        Resource resource1 = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName1, ext1));
        MockMultipartFile _file1 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName1, ext1),
                MediaType.TEXT_MARKDOWN_VALUE,
                resource1.getInputStream()
        );
        String fileName2 = "test";
        String ext2 = "gif";
        Resource resource2 = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName2, ext2));
        MockMultipartFile _file2 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName2, ext2),
                MediaType.IMAGE_GIF_VALUE,
                resource2.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.POST, "/api/reviews")
                        .file(_request)
                        .file(_file1)
                        .file(_file2)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code[0]").value("F-00-00-01"))
                .andExpect(jsonPath("code[1]").value("F-00-00-02"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(MediaType.TEXT_MARKDOWN_VALUE))
                .andExpect(jsonPath("data[1].field").exists())
                .andExpect(jsonPath("data[1].objectName").exists())
                .andExpect(jsonPath("data[1].code").exists())
                .andExpect(jsonPath("data[1].defaultMessage").exists())
                .andExpect(jsonPath("data[1].rejectedValue").value(MediaType.IMAGE_GIF_VALUE))
        ;

        checkNotCreated(checkList);
    }

    private List<Long> getCheckListNotCreated() {
        return List.of(
                this.reviewService.count() + 1,
                this.reviewHashtagService.count(),
                this.reviewImageService.count(),
                this.fileService.count()
        );
    }

    private void checkNotCreated(List<Long> checkList) {
        int i = 0;
        ResData resData = assertThrows(ApiResponseException.class, () -> this.reviewService.getReviewById(checkList.get(0))).getResData();
        assertThat(resData.getCode()).isEqualTo(ResCode.F_03_03_01.getCode());
        assertThat(this.reviewHashtagService.count()).isEqualTo(checkList.get(++i));
        assertThat(this.reviewImageService.count()).isEqualTo(checkList.get(++i));
        assertThat(this.fileService.count()).isEqualTo(checkList.get(++i));
    }

    @ParameterizedTest
    @MethodSource("argsFor_getReviews_OK")
    @DisplayName("get:/api/reviews - ok, S-03-02")
    public void getReviews_OK(Integer page, Integer size, String sort, Boolean image) throws Exception {

        // given
        Long spotId = 1L;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (page != null) params.add("page", page.toString());
        if (size != null) params.add("size", size.toString());
        if (!sort.isBlank()) params.add("sort", sort);
        params.add("spotId", spotId.toString());
        if (image != null) params.add("image", image.toString());

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/reviews?%s".formatted(AppConfig.getQueryString(params)))
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-03-02"))
                .andExpect(jsonPath("message").value(ResCode.S_03_02.getMessage()))
                .andExpect(jsonPath("data.list[0].id").exists())
                .andExpect(jsonPath("data.list[0].createDate").exists())
                .andExpect(jsonPath("data.list[0].visitDate").exists())
                .andExpect(jsonPath("data.list[0].author").exists())
                .andExpect(jsonPath("data.list[0].visitDate").exists())
                .andExpect(jsonPath("data.list[0].title").exists())
                .andExpect(jsonPath("data.list[0].content").exists())
                .andExpect(jsonPath("data.list[0].score").exists())
                .andExpect(jsonPath("data.list[0].status").exists())
                .andExpect(jsonPath("data.list[0].validated").exists())
                .andExpect(jsonPath("data.list[0].liked").exists())
                .andExpect(jsonPath("data.list[0]._links.self").exists())
                .andExpect(jsonPath("_links.self.href").value(AppConfig.getBaseURL() + ":8080/api/reviews?%s".formatted(AppConfig.getQueryString(params))))
                .andExpect(jsonPath("_links.profile").exists())
        ;

        if (page == null || page == 1) {
            resultActions
                    .andExpect(jsonPath("data.page").value(1))
            ;
        } else {
            resultActions
                    .andExpect(jsonPath("data.page").value(page))
            ;
        }

        if (size == null || size == 20) {
            resultActions
                    .andExpect(jsonPath("data.size").value(20))
            ;
        } else {
            resultActions
                    .andExpect(jsonPath("data.size").value(size))
            ;
        }

        if (sort.equals("like") || sort.isBlank()) {
            resultActions
                    .andExpect(jsonPath("data.sort[0].property").value("liked"))
                    .andExpect(jsonPath("data.sort[0].direction").value("desc"))
                    .andExpect(jsonPath("data.sort[1].property").value("create_date"))
                    .andExpect(jsonPath("data.sort[1].direction").value("asc"))
            ;
        } else if (sort.equals("new")) {
            resultActions
                    .andExpect(jsonPath("data.sort[0].property").value("create_date"))
                    .andExpect(jsonPath("data.sort[0].direction").value("desc"))
            ;
        } else if (sort.equals("old")) {
            resultActions
                    .andExpect(jsonPath("data.sort[0].property").value("create_date"))
                    .andExpect(jsonPath("data.sort[0].direction").value("asc"))
            ;
        }

        if (image != null && image) {
            int len = JsonPath.read(resultActions.andReturn().getResponse().getContentAsString(), "data.list.length()");
            for (int i = 0; i < len; i++) {
                resultActions
                        .andExpect(jsonPath("data.list[0].imageUri").exists())
                ;
            }
        }
    }

    private static Stream<Arguments> argsFor_getReviews_OK() {
        return Stream.of(
                Arguments.of(null, null, " ", null),
                Arguments.of(null, null, "liked", null),
                Arguments.of(null, null, "old", null),
                Arguments.of(null, null, "new", null),
                Arguments.of(null, null, " ", false),
                Arguments.of(null, null, "liked", false),
                Arguments.of(null, null, "old", false),
                Arguments.of(null, null, "new", false),
                Arguments.of(null, null, " ", true),
                Arguments.of(null, null, "liked", true),
                Arguments.of(null, null, "old", true),
                Arguments.of(null, null, "new", true),
                Arguments.of(null, 20, " ", null),
                Arguments.of(null, 20, "liked", null),
                Arguments.of(null, 20, "old", null),
                Arguments.of(null, 20, "new", null),
                Arguments.of(null, 20, " ", false),
                Arguments.of(null, 20, "liked", false),
                Arguments.of(null, 20, "old", false),
                Arguments.of(null, 20, "new", false),
                Arguments.of(null, 20, " ", true),
                Arguments.of(null, 20, "liked", true),
                Arguments.of(null, 20, "old", true),
                Arguments.of(null, 20, "new", true),
                Arguments.of(null, 50, " ", null),
                Arguments.of(null, 50, "liked", null),
                Arguments.of(null, 50, "old", null),
                Arguments.of(null, 50, "new", null),
                Arguments.of(null, 50, " ", false),
                Arguments.of(null, 50, "liked", false),
                Arguments.of(null, 50, "old", false),
                Arguments.of(null, 50, "new", false),
                Arguments.of(null, 50, " ", true),
                Arguments.of(null, 50, "liked", true),
                Arguments.of(null, 50, "old", true),
                Arguments.of(null, 50, "new", true),
                Arguments.of(null, 100, " ", null),
                Arguments.of(null, 100, "liked", null),
                Arguments.of(null, 100, "old", null),
                Arguments.of(null, 100, "new", null),
                Arguments.of(null, 100, " ", false),
                Arguments.of(null, 100, "liked", false),
                Arguments.of(null, 100, "old", false),
                Arguments.of(null, 100, "new", false),
                Arguments.of(null, 100, " ", true),
                Arguments.of(null, 100, "liked", true),
                Arguments.of(null, 100, "old", true),
                Arguments.of(null, 100, "new", true),
                Arguments.of(1, null, " ", null),
                Arguments.of(1, null, "liked", null),
                Arguments.of(1, null, "old", null),
                Arguments.of(1, null, "new", null),
                Arguments.of(1, null, " ", false),
                Arguments.of(1, null, "liked", false),
                Arguments.of(1, null, "old", false),
                Arguments.of(1, null, "new", false),
                Arguments.of(1, null, " ", true),
                Arguments.of(1, null, "liked", true),
                Arguments.of(1, null, "old", true),
                Arguments.of(1, null, "new", true),
                Arguments.of(1, 20, " ", null),
                Arguments.of(1, 20, "liked", null),
                Arguments.of(1, 20, "old", null),
                Arguments.of(1, 20, "new", null),
                Arguments.of(1, 20, " ", false),
                Arguments.of(1, 20, "liked", false),
                Arguments.of(1, 20, "old", false),
                Arguments.of(1, 20, "new", false),
                Arguments.of(1, 20, " ", true),
                Arguments.of(1, 20, "liked", true),
                Arguments.of(1, 20, "old", true),
                Arguments.of(1, 20, "new", true),
                Arguments.of(1, 50, " ", null),
                Arguments.of(1, 50, "liked", null),
                Arguments.of(1, 50, "old", null),
                Arguments.of(1, 50, "new", null),
                Arguments.of(1, 50, " ", false),
                Arguments.of(1, 50, "liked", false),
                Arguments.of(1, 50, "old", false),
                Arguments.of(1, 50, "new", false),
                Arguments.of(1, 50, " ", true),
                Arguments.of(1, 50, "liked", true),
                Arguments.of(1, 50, "old", true),
                Arguments.of(1, 50, "new", true),
                Arguments.of(1, 100, " ", null),
                Arguments.of(1, 100, "liked", null),
                Arguments.of(1, 100, "old", null),
                Arguments.of(1, 100, "new", null),
                Arguments.of(1, 100, " ", false),
                Arguments.of(1, 100, "liked", false),
                Arguments.of(1, 100, "old", false),
                Arguments.of(1, 100, "new", false),
                Arguments.of(1, 100, " ", true),
                Arguments.of(1, 100, "liked", true),
                Arguments.of(1, 100, "old", true),
                Arguments.of(1, 100, "new", true)
        );
    }

    @Test
    @DisplayName("get:/api/reviews - bad request spot not exist, F-03-02-01")
    public void getReviews_BadRequest_SpotNotExist() throws Exception {

        // given
        Long spotId = 10000000L;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("spotId", spotId.toString());

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/reviews?%s".formatted(AppConfig.getQueryString(params)))
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-03-02-01"))
                .andExpect(jsonPath("message").value(ResCode.F_03_02_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(spotId.toString()))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("get:/api/reviews - bad request not exist, F-03-02-02")
    public void getReviews_BadRequest_NotExist() throws Exception {

        // given
        Long spotId = 102L;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("spotId", spotId.toString());

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/reviews?%s".formatted(AppConfig.getQueryString(params)))
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-03-02-02"))
                .andExpect(jsonPath("message").value(ResCode.F_03_02_02.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(spotId.toString()))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("get:/api/reviews - bad request not exist, F-03-02-03")
    public void getReviews_BadRequest_SizeNotAllowed() throws Exception {

        // given
        Integer size = 22;
        Long spotId = 1L;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("size", size.toString());
        params.add("spotId", spotId.toString());

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/reviews?%s".formatted(AppConfig.getQueryString(params)))
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-03-02-03"))
                .andExpect(jsonPath("message").value(ResCode.F_03_02_03.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(size.toString()))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("get:/api/reviews/{id} - ok, S-03-03")
    public void getReview_OK() throws Exception {

        // given
        Long id = 1L;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/reviews/%s".formatted(id))
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(ResCode.S_03_03.getStatus().name()))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value(ResCode.S_03_03.getCode()))
                .andExpect(jsonPath("message").value(ResCode.S_03_03.getMessage()))
                .andExpect(jsonPath("data.id").value(id))
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.visitDate").exists())
                .andExpect(jsonPath("data.author").exists())
                .andExpect(jsonPath("data.visitDate").exists())
                .andExpect(jsonPath("data.title").exists())
                .andExpect(jsonPath("data.content").exists())
                .andExpect(jsonPath("data.score").exists())
                .andExpect(jsonPath("data.status").exists())
                .andExpect(jsonPath("data.validated").exists())
                .andExpect(jsonPath("data.liked").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Test
    @DisplayName("get:/api/reviews/{id} - bad request not exist, F-03-03-01")
    public void getReview_BadRequest_NotExist() throws Exception {

        // given
        Long id = 100000000L;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/reviews/%s".formatted(id))
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ResCode.F_03_03_01.getStatus().name()))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value(ResCode.F_03_03_01.getCode()))
                .andExpect(jsonPath("message").value(ResCode.F_03_03_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(id.toString()))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Transactional
    @ParameterizedTest
    @MethodSource("argsFor_updateReview_OK")
    @DisplayName("patch:/api/reviews/{id} - ok, S-03-04")
    public void updateReview_OK(String title,
                                String content,
                                Double score,
                                String hashtag,
                                Boolean lock,
                                String fileName,
                                String ext) throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Review review = this.reviewService.getReviewById(id);
        Spot spot = this.spotService.getSpotById(review.getSpot().getId());
        Double averageScore = score != null ?
                (spot.getAverageScore() * spot.getReviews().size() - review.getScore() + score) / spot.getReviews().size() : spot.getAverageScore();
        List<String> hashtags = hashtag != null ? List.of(hashtag) : null;
        ReviewRequest.UpdateReview request = ReviewRequest.UpdateReview.builder()
                .title(title)
                .content(content)
                .score(score)
                .hashtags(hashtags)
                .lock(lock)
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        MockMultipartFile _file = (fileName.isBlank() || ext.isBlank()) ? null :
                new MockMultipartFile(
                        "images",
                        "%s.%s".formatted(fileName, ext),
                        MediaType.IMAGE_PNG_VALUE,
                        resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName, ext)).getInputStream()
                );
        List<MockMultipartFile> files = _file != null ? List.of(_file) : null;

        // when
        ResultActions resultActions = _file != null ? this.mockMvc
                .perform(multipart(HttpMethod.PATCH, "/api/reviews/%s".formatted(id))
                        .file(_request)
                        .file(_file)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print()) : this.mockMvc
                .perform(multipart(HttpMethod.PATCH, "/api/reviews/%s".formatted(id))
                        .file(_request)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-03-04"))
                .andExpect(jsonPath("message").value(ResCode.S_03_04.getMessage()))
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.spot.averageScore").value("%.2f".formatted(averageScore)))
                .andExpect(jsonPath("data.title").value(title.isBlank() ? review.getTitle() : title))
                .andExpect(jsonPath("data.content").value(content.isBlank() ? review.getContent() : content))
                .andExpect(jsonPath("data.score").value(score == null ? review.getScore() : score))
                .andExpect(jsonPath("data.hashtags[0]").value(hashtag))
                .andExpect(jsonPath("data.imageUri[0]").exists())
                .andExpect(jsonPath("data.status").value(lock != null ? (lock ? ReviewStatus.PRIVATE.getType() : ReviewStatus.PUBLIC.getType()) : review.getStatus().getType()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;

        if (hashtags != null) {
            List<ReviewHashtag> reviewHashtags = this.reviewHashtagService.getAllByReview(review);
            assertThat(reviewHashtags.size()).isEqualTo(hashtags.size());
            for (int i = 0; i < reviewHashtags.size(); i++) {
                assertThat(reviewHashtags.get(i).getHashtag().getName()).isEqualTo(hashtags.get(i));
            }
        }
        if (files != null) {
            List<ReviewImage> reviewImages = this.reviewImageService.getAllByReview(review);
            assertThat(reviewImages.size()).isEqualTo(files.size());
            for (int i = 0; i < reviewImages.size(); i++) {
                assertThat(reviewImages.get(i).getImage().getName()).isEqualTo(files.get(i).getOriginalFilename());
                assertThat(reviewImages.get(i).getImage().getExt()).isEqualTo(files.get(i).getContentType().split("/")[1]);
            }
        }
    }

    private static Stream<Arguments> argsFor_updateReview_OK() {
        return Stream.of(
                Arguments.of("수정 테스트 제목", "수정 테스트 내용", 4.0, "수정태그", true, "test_update", "png"),
                Arguments.of("", "수정 테스트 내용", 4.0, "수정태그", true, "test_update", "png"),
                Arguments.of("수정 테스트 제목", "", 4.0, "수정태그", true, "test_update", "png"),
                Arguments.of("수정 테스트 제목", "수정 테스트 내용", null, "수정태그", true, "test_update", "png"),
                Arguments.of("수정 테스트 제목", "수정 테스트 내용", 4.0, "수정태그", null, "test_update", "png"),
                Arguments.of("수정 테스트 제목", "수정 테스트 내용", 4.0, "수정태그", true, "", ""),
                Arguments.of("수정 테스트 제목", "수정 테스트 내용", 4.0, "수정태그", true, "test_update", ""),
                Arguments.of("수정 테스트 제목", "수정 테스트 내용", 4.0, "수정태그", true, "", "png")
        );
    }

    @Test
    @DisplayName("patch:/api/reviews/{id} - bad request not exist, F-03-04-01")
    public void updateReview_BadRequest_NotExist() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1000000000L;
        ReviewRequest.UpdateReview request = ReviewRequest.UpdateReview.builder().build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.PATCH, "/api/reviews/%s".formatted(id))
                        .file(_request)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-03-04-01"))
                .andExpect(jsonPath("message").value(ResCode.F_03_04_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(id.toString()))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("patch:/api/reviews/{id} - bad request not allowed, F-03-04-02")
    public void updateReview_BadRequest_NotAllowed() throws Exception {

        // given
        String username = "user2";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Review before = this.reviewService.getReviewById(id);
        List<ReviewHashtag> beforeHashtags = this.reviewHashtagService.getAllByReview(before);
        List<ReviewImage> beforeImages = this.reviewImageService.getAllByReview(before);
        Spot beforeSpot = this.spotService.getSpotById(before.getSpot().getId());
        String hashTag = " ";
        ReviewRequest.UpdateReview request = ReviewRequest.UpdateReview.builder().build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.PATCH, "/api/reviews/%s".formatted(id))
                        .file(_request)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-03-04-02"))
                .andExpect(jsonPath("message").value(ResCode.F_03_04_02.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(username))
                .andExpect(jsonPath("_links.index").exists())
        ;

        Review after = this.reviewService.getReviewById(id);
        checkNotUpdated(before, beforeHashtags, beforeImages, beforeSpot, after);
    }

    @Test
    @DisplayName("patch:/api/reviews/{id} - bad request not blank, F-03-04-03")
    public void updateReview_BadRequest_NotBlank() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Review before = this.reviewService.getReviewById(id);
        List<ReviewHashtag> beforeHashtags = this.reviewHashtagService.getAllByReview(before);
        List<ReviewImage> beforeImages = this.reviewImageService.getAllByReview(before);
        Spot beforeSpot = this.spotService.getSpotById(before.getSpot().getId());
        String hashtag = " ";
        ReviewRequest.UpdateReview request = ReviewRequest.UpdateReview.builder()
                .hashtags(List.of(hashtag))
                .build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.PATCH, "/api/reviews/%s".formatted(id))
                        .file(_request)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-03-04-03"))
                .andExpect(jsonPath("message").value(ResCode.F_03_04_03.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(hashtag))
                .andExpect(jsonPath("_links.index").exists())
        ;

        Review after = this.reviewService.getReviewById(id);
        checkNotUpdated(before, beforeHashtags, beforeImages, beforeSpot, after);
    }

    @Test
    @DisplayName("patch:/api/reviews/{id} - bad request content type, F-00-00-01")
    public void updateReview_BadRequest_ContentType() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Review before = this.reviewService.getReviewById(id);
        List<ReviewHashtag> beforeHashtags = this.reviewHashtagService.getAllByReview(before);
        List<ReviewImage> beforeImages = this.reviewImageService.getAllByReview(before);
        Spot beforeSpot = this.spotService.getSpotById(before.getSpot().getId());
        ReviewRequest.UpdateReview request = ReviewRequest.UpdateReview.builder().build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String fileName = "test";
        String ext = "a";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.TEXT_MARKDOWN_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.PATCH, "/api/reviews/%s".formatted(id))
                        .file(_request)
                        .file(_file)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-00-00-01"))
                .andExpect(jsonPath("message").value(ResCode.F_00_00_01.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(MediaType.TEXT_MARKDOWN_VALUE))
                .andExpect(jsonPath("_links.index").exists())
        ;

        Review after = this.reviewService.getReviewById(id);
        checkNotUpdated(before, beforeHashtags, beforeImages, beforeSpot, after);
    }

    @Test
    @DisplayName("patch:/api/reviews/{id} - bad request extension, F-00-00-02")
    public void updateReview_BadRequest_Extension() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Review before = this.reviewService.getReviewById(id);
        List<ReviewHashtag> beforeHashtags = this.reviewHashtagService.getAllByReview(before);
        List<ReviewImage> beforeImages = this.reviewImageService.getAllByReview(before);
        Spot beforeSpot = this.spotService.getSpotById(before.getSpot().getId());
        ReviewRequest.UpdateReview request = ReviewRequest.UpdateReview.builder().build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String fileName1 = "test";
        String ext1 = "a";
        Resource resource1 = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName1, ext1));
        MockMultipartFile _file1 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName1, ext1),
                MediaType.TEXT_MARKDOWN_VALUE,
                resource1.getInputStream()
        );
        String fileName2 = "test";
        String ext2 = "gif";
        Resource resource2 = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName2, ext2));
        MockMultipartFile _file2 = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName2, ext2),
                MediaType.IMAGE_GIF_VALUE,
                resource2.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.PATCH, "/api/reviews/%s".formatted(id))
                        .file(_request)
                        .file(_file1)
                        .file(_file2)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code[0]").value("F-00-00-01"))
                .andExpect(jsonPath("code[1]").value("F-00-00-02"))
                .andExpect(jsonPath("message[0]").value(ResCode.F_00_00_01.getMessage()))
                .andExpect(jsonPath("message[1]").value(ResCode.F_00_00_02.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(MediaType.TEXT_MARKDOWN_VALUE))
                .andExpect(jsonPath("data[1].field").exists())
                .andExpect(jsonPath("data[1].objectName").exists())
                .andExpect(jsonPath("data[1].code").exists())
                .andExpect(jsonPath("data[1].defaultMessage").exists())
                .andExpect(jsonPath("data[1].rejectedValue").value(MediaType.IMAGE_GIF_VALUE))
                .andExpect(jsonPath("_links.index").exists())
        ;

        Review after = this.reviewService.getReviewById(id);
        checkNotUpdated(before, beforeHashtags, beforeImages, beforeSpot, after);
    }

    @Test
    @DisplayName("patch:/api/reviews/{id} - bad request multiple file error, F-00-00-01 & F-00-00-02")
    public void updateReview_BadRequest_MultipleFileError() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Review before = this.reviewService.getReviewById(id);
        List<ReviewHashtag> beforeHashtags = this.reviewHashtagService.getAllByReview(before);
        List<ReviewImage> beforeImages = this.reviewImageService.getAllByReview(before);
        Spot beforeSpot = this.spotService.getSpotById(before.getSpot().getId());
        ReviewRequest.UpdateReview request = ReviewRequest.UpdateReview.builder().build();
        MockMultipartFile _request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                this.objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );

        String fileName = "test";
        String ext = "gif";
        Resource resource = resourceLoader.getResource("classpath:/static/image/%s.%s".formatted(fileName, ext));
        MockMultipartFile _file = new MockMultipartFile(
                "images",
                "%s.%s".formatted(fileName, ext),
                MediaType.IMAGE_GIF_VALUE,
                resource.getInputStream()
        );

        // when
        ResultActions resultActions = this.mockMvc
                .perform(multipart(HttpMethod.PATCH, "/api/reviews/%s".formatted(id))
                        .file(_request)
                        .file(_file)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-00-00-02"))
                .andExpect(jsonPath("message").value(ResCode.F_00_00_02.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(MediaType.IMAGE_GIF_VALUE))
                .andExpect(jsonPath("_links.index").exists())
        ;

        Review after = this.reviewService.getReviewById(id);
        checkNotUpdated(before, beforeHashtags, beforeImages, beforeSpot, after);
    }

    private void checkNotUpdated(Review before, List<ReviewHashtag> beforeHashtags, List<ReviewImage> beforeImages, Spot beforeSpot, Review after) {
        assertThat(before.getTitle()).isEqualTo(after.getTitle());
        assertThat(before.getContent()).isEqualTo(after.getContent());
        assertThat(before.getScore()).isEqualTo(after.getScore());
        assertThat(before.getStatus()).isEqualTo(after.getStatus());
        List<ReviewHashtag> afterHashtags = this.reviewHashtagService.getAllByReview(after);
        assertThat(beforeHashtags.size()).isEqualTo(afterHashtags.size());
        for (int i = 0; i < beforeHashtags.size(); i++) {
            assertThat(beforeHashtags.get(i).getHashtag().getName()).isEqualTo(afterHashtags.get(i).getHashtag().getName());
        }
        List<ReviewImage> afterImages = this.reviewImageService.getAllByReview(after);
        assertThat(beforeImages.size()).isEqualTo(afterImages.size());
        for (int i = 0; i < beforeImages.size(); i++) {
            assertThat(beforeImages.get(i).getImage().getUuid()).isEqualTo(afterImages.get(i).getImage().getUuid());
        }
        Spot afterSpot = this.spotService.getSpotById(after.getSpot().getId());
        assertThat(beforeSpot.getAverageScore()).isEqualTo(afterSpot.getAverageScore());
    }

    @Transactional
    @Test
    @DisplayName("delete:/api/reviews/{id} - ok, S-03-05")
    public void deleteReview_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(delete("/api/reviews/%s".formatted(id))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(ResCode.S_03_05.getStatus().name()))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value(ResCode.S_03_05.getCode()))
                .andExpect(jsonPath("message").value(ResCode.S_03_05.getMessage()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;

        ResData resData = assertThrows(ApiResponseException.class, () -> this.reviewService.getReviewById(id)).getResData();
        assertThat(resData.getCode()).isEqualTo(ResCode.F_03_03_01.getCode());
    }

    @Test
    @DisplayName("delete:/api/reviews/{id} - bad request not exist, F-03-05-01")
    public void deleteReview_BadRequest_NotExist() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 100000000L;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(delete("/api/reviews/%s".formatted(id))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ResCode.F_03_05_01.getStatus().name()))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value(ResCode.F_03_05_01.getCode()))
                .andExpect(jsonPath("message").value(ResCode.F_03_05_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(id.toString()))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("delete:/api/reviews/{id} - bad request not allowed, F-03-05-02")
    public void deleteReview_BadRequest_NotAllowed() throws Exception {

        // given
        String username = "user2";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(delete("/api/reviews/%s".formatted(id))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(ResCode.F_03_05_02.getStatus().name()))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value(ResCode.F_03_05_02.getCode()))
                .andExpect(jsonPath("message").value(ResCode.F_03_05_02.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(username))
                .andExpect(jsonPath("_links.index").exists())
        ;

        assertDoesNotThrow(() -> this.reviewService.getReviewById(id));
    }

    @Transactional
    @Test
    @DisplayName("patch:/api/reviews/{id}/like - ok like, S-03-06")
    public void likeReview_OK_Like() throws Exception {

        // given
        String username = "user2";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Long liked = this.reviewService.getReviewById(id).getLiked();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/reviews/%s/like".formatted(id))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-03-06"))
                .andExpect(jsonPath("message").value(ResCode.S_03_06.getMessage()))
                .andExpect(jsonPath("data.liked").value(liked + 1))
                .andExpect(jsonPath("data.like").value(true))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Transactional
    @Test
    @DisplayName("patch:/api/reviews/{id}/like - ok undo like, S-03-06")
    public void likeReview_OK_UndoLike() throws Exception {

        // given
        String username = "user2";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Review review = this.reviewService.getReviewById(1L).toBuilder()
                .liked(1L)
                .likedMember(Set.of(this.memberService.getUserByUsername(username)))
                .build();
        this.reviewRepository.save(review);
        Long liked = review.getLiked();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/reviews/%s/like".formatted(id))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-03-06"))
                .andExpect(jsonPath("message").value(ResCode.S_03_06.getMessage()))
                .andExpect(jsonPath("data.liked").value(liked - 1))
                .andExpect(jsonPath("data.like").value(false))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Test
    @DisplayName("patch:/api/reviews/{id}/like - bad request not exist, F-03-06-01")
    public void likeReview_BadRequest_NotExist() throws Exception {

        // given
        String username = "user2";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 10000000L;

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/reviews/%s/like".formatted(id))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-03-06-01"))
                .andExpect(jsonPath("message").value(ResCode.F_03_06_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(id))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("patch:/api/reviews/{id}/like - bad request not allowed, F-03-06-02")
    public void likeReview_BadRequest_NotAllowed() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long id = 1L;
        Review before = this.reviewService.getReviewById(id);

        // when
        ResultActions resultActions = this.mockMvc
                .perform(patch("/api/reviews/%s/like".formatted(id))
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-03-06-02"))
                .andExpect(jsonPath("message").value(ResCode.F_03_06_02.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(username))
                .andExpect(jsonPath("_links.index").exists())
        ;

        Review after = this.reviewService.getReviewById(id);
        checkNotLiked(before, after);
    }

    private void checkNotLiked(Review before, Review after) {
        assertThat(before.getLiked()).isEqualTo(after.getLiked());
    }
}