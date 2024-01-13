package com.cojar.whats_hot_backend.domain.spot_module.category.controller;

import com.cojar.whats_hot_backend.domain.base_module.file.service.FileService;
import com.cojar.whats_hot_backend.domain.spot_module.category.request.CategoryRequest;
import com.cojar.whats_hot_backend.domain.spot_module.category.service.CategoryService;
import com.cojar.whats_hot_backend.domain.spot_module.menu_item.service.MenuItemService;
import com.cojar.whats_hot_backend.domain.spot_module.spot.service.SpotService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_hashtag.service.SpotHashtagService;
import com.cojar.whats_hot_backend.domain.spot_module.spot_image.service.SpotImageService;
import com.cojar.whats_hot_backend.global.controller.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

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

    private static Stream<Arguments> argsFor_createCategory_Created() {
        return Stream.of(
                Arguments.of("항공권", 1, null),
                Arguments.of("퓨전요리", 2, 1L),
                Arguments.of("카라반", 3, 245L)
        );
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

    private static Stream<Arguments> argsFor_createCategory_BadRequest_NotBlank() {
        return Stream.of(
                Arguments.of("", 1, null)    // 이름값이 빈 문자열일 때
        );
    }

}