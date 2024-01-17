package com.cojar.whats_hot_backend.domain.spot_module.category.controller;

import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.spot_module.category.request.CategoryRequest;
import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.service.MenuItemService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.service.SpotHashtagService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.service.SpotImageService;
import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;
import com.cojar.whats_hot_backend.global.response.ResCode;
import com.cojar.whats_hot_backend.global.util.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends BaseControllerTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SpotService spotService;

    @Autowired
    private SpotHashtagService spotHashtagService;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private SpotImageService spotImageService;

    @Autowired
    private FileService fileService;

    private static Stream<Arguments> argsFor_createCategory_Created() {
        return Stream.of(
                Arguments.of("항공권", 1, null),
                Arguments.of("퓨전요리", 2, 1L),
                Arguments.of("카라반", 3, 245L)
        );
    }

    private static Stream<Arguments> argsFor_createCategory_BadRequest_NotBlank() {
        return Stream.of(
                Arguments.of("", 1, null)    // 이름값이 빈 문자열일 때
        );
    }

    @Transactional
    @ParameterizedTest
    @MethodSource("argsFor_createCategory_Created")
    @DisplayName("post:/api/categories - created, S-05-01")
    public void createCategory_Created(String name, Integer depth, Long parentId) throws Exception {

        // given
        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        CategoryRequest.CreateCategory request = CategoryRequest.CreateCategory.builder()
                .name(name)
                .depth(depth)
                .parentId(parentId)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/categories")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-05-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.depth").value(depth))
                .andExpect(jsonPath("data.name").value(name))
        ;
        if (parentId != null) resultActions.andExpect(jsonPath("data.parentId").value(parentId));
    }

    @Transactional
    @ParameterizedTest
    @MethodSource("argsFor_createCategory_BadRequest_NotBlank")
    @DisplayName("post:/api/categories - created, F-05-01-01")
    public void createCategory_BadRequest_NotBlank(String name, Integer depth, Long parentId) throws Exception {

        // given
        String username = "admin";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        CategoryRequest.CreateCategory request = CategoryRequest.CreateCategory.builder()
                .name(name)
                .depth(depth)
                .parentId(parentId)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/categories")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-05-01-01"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data").doesNotExist())
        ; // No data expected on failure
        if (parentId != null) resultActions.andExpect(jsonPath("data.parentId").value(parentId));
    }

    @ParameterizedTest
    @MethodSource("argsFor_getCategories_OK")
    @DisplayName("get:/api/categories - ok, S-05-02")
    public void getCategories_OK(Long parentId) throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (parentId != -1) params.add("parentId", parentId.toString());

        String query = AppConfig.getBaseURL() + ":8080/api/categories" + (AppConfig.getQueryString(params).isBlank() ? "" : "?%s".formatted(AppConfig.getQueryString(params)));

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/categories?%s".formatted(AppConfig.getQueryString(params)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-05-02"))
                .andExpect(jsonPath("message").value(ResCode.S_05_02.getMessage()))
                .andExpect(jsonPath("data.list[0].id").exists())
                .andExpect(jsonPath("data.list[0].name").exists())
                .andExpect(jsonPath("data.list[0]._links.self").exists())
                .andExpect(jsonPath("_links.self.href").value(query))
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    private static Stream<Arguments> argsFor_getCategories_OK() {
        return Stream.of(
                Arguments.of(-1L),
                Arguments.of(1L),
                Arguments.of(2L),
                Arguments.of(3L),
                Arguments.of(4L),
                Arguments.of(5L),
                Arguments.of(6L),
                Arguments.of(7L),
                Arguments.of(8L),
                Arguments.of(25L),
                Arguments.of(37L)
        );
    }

    @Test
    @DisplayName("get:/api/categories - bad request not exist, F-05-02-01")
    public void getCategories_BadRequest_NotExist() throws Exception {

        // given
        Long parentId = 100000L;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (parentId != -1) params.add("parentId", parentId.toString());

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/categories?%s".formatted(AppConfig.getQueryString(params)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-05-02-01"))
                .andExpect(jsonPath("message").value(ResCode.F_05_02_01.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(parentId))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @ParameterizedTest
    @MethodSource("argsFor_getCategories_BadRequest_InvalidDepth")
    @DisplayName("get:/api/categories - bad request invalid depth, F-05-02-02")
    public void getCategories_BadRequest_InvalidDepth(Long parentId) throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (parentId != -1) params.add("parentId", parentId.toString());

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/categories?%s".formatted(AppConfig.getQueryString(params)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-05-02-02"))
                .andExpect(jsonPath("message").value(ResCode.F_05_02_02.getMessage()))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue[0]").value(parentId))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    private static Stream<Arguments> argsFor_getCategories_BadRequest_InvalidDepth() {
        return Stream.of(
                Arguments.of(9L),
                Arguments.of(10L),
                Arguments.of(11L),
                Arguments.of(12L),
                Arguments.of(13L),
                Arguments.of(14L),
                Arguments.of(15L),
                Arguments.of(16L),
                Arguments.of(17L),
                Arguments.of(18L),
                Arguments.of(19L),
                Arguments.of(20L),
                Arguments.of(21L),
                Arguments.of(22L),
                Arguments.of(23L),
                Arguments.of(24L),
                Arguments.of(26L),
                Arguments.of(27L),
                Arguments.of(28L),
                Arguments.of(29L),
                Arguments.of(30L),
                Arguments.of(31L),
                Arguments.of(32L),
                Arguments.of(33L),
                Arguments.of(34L),
                Arguments.of(35L),
                Arguments.of(36L),
                Arguments.of(38L),
                Arguments.of(39L),
                Arguments.of(40L),
                Arguments.of(41L),
                Arguments.of(42L),
                Arguments.of(43L),
                Arguments.of(44L)
        );
    }
}