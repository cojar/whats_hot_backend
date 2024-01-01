package com.cojar.whats_hot_backend.domain.review_module.review.controller;

import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.review_module.review.entity.ReviewStatus;
import com.cojar.whats_hot_backend.domain.review_module.review.request.ReviewRequest;
import com.cojar.whats_hot_backend.domain.review_module.review.service.ReviewService;
import com.cojar.whats_hot_backend.domain.review_module.review_hashtag.service.ReviewHashtagService;
import com.cojar.whats_hot_backend.domain.review_module.review_image.service.ReviewImageService;
import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;
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

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
        assertThat(this.reviewService.getReviewById(checkList.get(i++))).isNull();
        assertThat(this.reviewHashtagService.count()).isEqualTo(checkList.get(i++));
        assertThat(this.reviewImageService.count()).isEqualTo(checkList.get(i++));
        assertThat(this.fileService.count()).isEqualTo(checkList.get(i));
    }
}